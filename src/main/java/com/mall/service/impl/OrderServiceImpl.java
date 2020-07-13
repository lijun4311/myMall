package com.mall.service.impl;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mall.common.Rest;
import com.mall.common.annotation.ArgsNotEmpty;
import com.mall.common.consts.AlipayConst;
import com.mall.common.consts.OrderEnum;
import com.mall.common.consts.PayPlatformEnum;
import com.mall.common.consts.PaymentTypeEnum;
import com.mall.common.consts.propertiesconsts.FtpConsts;
import com.mall.common.myexception.RestException;
import com.mall.entity.*;
import com.mall.mapper.OrderMapper;
import com.mall.service.*;
import com.mall.util.*;
import com.mall.vo.out.OrderItemVo;
import com.mall.vo.out.OrderProductVo;
import com.mall.vo.out.OrderVo;
import com.mall.vo.out.ShippingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mall.common.consts.ProductEnum.ON_SALE;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;
    @Autowired
    ShippingService shippingService;
    @Autowired
    OrderItemService orderItemService;
    private static AlipayTradeService tradeService;
    @Autowired
    private PayInfoService payInfoService;

    static {

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("properties/alipay.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Override
    @ArgsNotEmpty
    public OrderVo createOrder(Integer id, Integer shippingId) {
        //准备数据
        //从购物车中获取数据
        List<Cart> cartList = cartService.selectCheckCarts(id);
        if (CollectionUtils.isEmpty(cartList)) {
            throw RestException.throwExcption("无购物车商品");
        }
        //获得订单号
        long orderNo = this.generateOrderNo();
        //生成订单子数据
        List<OrderItem> orderItemList = this.getCartOrderItem(id, cartList, orderNo);
        if (MyBeanUtil.isRequired(orderItemList)) {
            throw RestException.throwExcption("商品状态异常");
        }
        //计算这个订单的总价
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);
        Order order = this.assembleOrder(id, shippingId, payment, orderNo);

        //数据存储
        order = orderProcessing(order, cartList, orderItemList);
        //数据封装
        return assembleOrderVo(order, orderItemList);
    }

    /**
     * 订单数据持久化
     *
     * @param order         订单
     * @param cartList      购物车集合
     * @param orderItemList 子订单集合
     * @return 订单
     */
    @Transactional(rollbackFor = Exception.class)
    @ArgsNotEmpty
    protected Order orderProcessing(Order order, List<Cart> cartList, List<OrderItem> orderItemList) {
        //生成订单  批量插入  减少我们产品的库存 清空一下购物车
        if (!(this.save(order)
                && orderItemService.saveBatch(orderItemList)
                && this.reduceProductStock(orderItemList)
                && cartService.removeByIds(cartList.stream().map(Cart::getId).collect(Collectors.toSet()))
        )) {
            throw RestException.throwExcption("生成订单错误");
        }
        return order;
    }

    /**
     * 购物车集合生成子订单集合
     *
     * @param userId   用户id
     * @param cartList 购物车对象集合
     * @return 子订单表集合 null 为物品信息变动
     */
    @ArgsNotEmpty("orderNo")
    private List<OrderItem> getCartOrderItem(Integer userId, List<Cart> cartList, Long orderNo) {
        List<OrderItem> orderItemList = Lists.newArrayList();
        Map<Integer, Product> productMap = productService.listByIds(Lists.transform(cartList, Cart::getProductId))
                .stream().collect(Collectors.toMap(BaseEntity::getId, Function.identity(), (key1, key2) -> key2));
        //校验购物车的数据,包括产品的状态和数量
        for (Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMap.get(cartItem.getProductId());
            if ((MyBeanUtil.isNull(product)) || (ON_SALE.getCode() != product.getStatus()) || (cartItem.getQuantity() > product.getStock())) {
                return null;
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartItem.getQuantity()));
            orderItem.setOrderNo(orderNo);
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }


    /**
     * 生成订单
     *
     * @param userId     用户id'
     * @param shippingId 购物地址id
     * @param payment    订单总价
     * @param orderNo    订单号
     * @return 订单对象
     */
    @ArgsNotEmpty
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment, Long orderNo) {
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setStatus(OrderEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        return order;
    }


    /**
     * 简单订单编号生成
     *
     * @return 订单编号
     */
    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    /**
     * 订单总价计算
     *
     * @param orderItemList 子订单集合
     * @return 总价
     */
    @ArgsNotEmpty
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    /**
     * 库存扣除
     *
     * @param orderItemList 订单集合
     */
    @ArgsNotEmpty
    private boolean reduceProductStock(List<OrderItem> orderItemList) {
        Map<Integer, Product> productMap = productService.list(
                new LambdaQueryWrapper<Product>()
                        .in(Product::getId, Lists.transform(orderItemList, OrderItem::getProductId))
                        .last("for update")
        ).stream().collect(Collectors.toMap(BaseEntity::getId, Function.identity(), (key1, key2) -> key2));
        if (CollectionUtils.isEmpty(productMap)) {
            return false;
        }
        for (OrderItem orderItem : orderItemList) {
            Product product = productMap.get(orderItem.getProductId());
            if (MyBeanUtil.isRequired(product)) {
                return false;
            }
            if (!productService.update(new LambdaUpdateWrapper<Product>()
                    .setSql("stock = stock - " + orderItem.getQuantity()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 订单数据封装
     *
     * @param orderItem 订单
     * @return vo
     */
    @ArgsNotEmpty
    private OrderItemVo assembleOrderItemVo(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(MyDateUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    @Override
    @ArgsNotEmpty
    public boolean cancel(Integer id, Long orderNo) {
        Order order = getUserOrder(id, orderNo);
        if (MyBeanUtil.isRequired(order)) {
            throw RestException.throwExcption("订单不存在");
        }
        LambdaUpdateWrapper<Order> updateOrder = new LambdaUpdateWrapper<Order>();
        updateOrder.eq(Order::getId, order.getId());
        updateOrder.set(Order::getStatus, OrderEnum.CANCELED.getCode());

        List<OrderItem> orderList = orderItemService.getUserOrderItem(orderNo, id);
        orderList.forEach(orderItem -> productService.update(new LambdaUpdateWrapper<Product>()
                .setSql("stock = stock + " + orderItem.getQuantity())));
        return this.update(updateOrder);
    }

    @ArgsNotEmpty
    @Override
    public Order getUserOrder(Integer id, Long orderNo) {
        return this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo).eq(Order::getUserId, id));
    }


    @Override
    public OrderProductVo getOrderCartProduct(Integer id) {
        OrderProductVo orderProductVo = new OrderProductVo();
        //从购物车中获取数据
        List<Cart> cartList = cartService.selectCheckCarts(id);
        List<OrderItem> orderItemList = this.getCartOrderItem(id, cartList, null);
        if (null == orderItemList) {
            throw RestException.throwExcption("商品状态异常");
        }
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost(FtpConsts.HTTP_PREFIX);
        return orderProductVo;
    }

    /**
     * 订单返回数据封装
     *
     * @param order         订单
     * @param orderItemList 子订单集合
     * @return 订单vo
     */
    @ArgsNotEmpty
    @Override
    public OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(PaymentTypeEnum.codeOf(order.getPaymentType()));

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(OrderEnum.codeOf(order.getStatus()));

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingService.getById(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(assembleShippingVo(shipping));
        }
        orderVo.setPaymentTime(MyDateUtil.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(MyDateUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(MyDateUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(MyDateUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(MyDateUtil.dateToStr(order.getCloseTime()));
        orderVo.setImageHost(FtpConsts.HTTP_PREFIX);
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    /**
     * 购物地址封装
     *
     * @param shipping 购物地址对象
     * @return 购物地址vo
     */
    @ArgsNotEmpty
    private ShippingVo assembleShippingVo(Shipping shipping) {
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }


    @Override
    @ArgsNotEmpty
    public OrderVo getUserOrderDetail(Integer id, Long orderNo) {
        Order order = this.getUserOrder(id, orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemService.getUserOrderItem(orderNo, id);
            return assembleOrderVo(order, orderItemList);
        }
        return null;
    }

    @Override
    @ArgsNotEmpty
    public OrderVo getOrderDetail(Long orderNo) {
        Order order = this.getOrder(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemService.getOrderItem(orderNo);
            return assembleOrderVo(order, orderItemList);
        }
        return null;
    }


    @Override
    @ArgsNotEmpty("userId")
    public List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId) {
        List<OrderVo> orderVoList = Lists.newArrayList();
        List<OrderItem> orderItemList = orderItemService.list(new LambdaQueryWrapper<OrderItem>()
                .in(OrderItem::getOrderNo, orderList.stream().map(Order::getOrderNo).collect(Collectors.toSet())).eq(userId>0,OrderItem::getUserId, userId));
        Map<Long, List<OrderItem>> orederMap = new HashMap<>(10);
        orderItemList.forEach(
                orderItem ->
                {
                    if (orederMap.containsKey(orderItem.getOrderNo())) {
                        orederMap.get(orderItem.getOrderNo()).add(orderItem);
                    } else {
                        orederMap.put(orderItem.getOrderNo(), Lists.newArrayList(orderItem));
                    }
                }
        );
        orderList.forEach(order -> orderVoList.add(assembleOrderVo(order, Optional.ofNullable(orederMap.get(order.getOrderNo())).orElse(Lists.newArrayList()))));
        return orderVoList;
    }


    @Override
    @ArgsNotEmpty
    public Rest pay(Long orderNo, Integer id) {
        //准备支付数据
        Order order = this.getUserOrder(id, orderNo);
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        String outTradeNo = order.getOrderNo().toString();
        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "扫码支付,订单号:" + outTradeNo;
        // (必填) 订单总金额，单位为元，不能超过1亿元  如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String totalAmount = order.getPayment().toString();
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";
        // (可选) 订单不可打折金额，  如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)  如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "订单" + outTradeNo + "购买商品共" + totalAmount + "元";
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";
        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");
        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";
        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<>();
        List<OrderItem> orderItemList = orderItemService.getUserOrderItem(orderNo, id);
        for (OrderItem orderItem : orderItemList) {
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(), BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), 100d).longValue(), orderItem.getQuantity());
            goodsDetailList.add(goods);
        }
        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder().setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body).setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams).setTimeoutExpress(timeoutExpress)
                //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setNotifyUrl(PropertiesUtil.getSystemConfig("ALIPAY_CALLBACK_URL", "http://www.mhs66.cn/myMall/order/alipayCallback")).setGoodsDetailList(goodsDetailList);
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                String path = Objects.requireNonNull(getClass().getClassLoader().getResource("/")).getPath() + PropertiesUtil.getSystemConfig("PAY_ORCODE_PATH", "QrPayCode");
                AlipayTradePrecreateResponse response = result.getResponse();
                File folder = new File(path);
                if (!folder.exists()) {
                    folder.setWritable(true);
                    if (!folder.mkdirs()) {
                        log.error("创建文件路径失败");
                        return Rest.errorMsg("支付宝预下单失败!!!");
                    }
                }
                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                File targetFile = new File(path, qrFileName);
                try {
                    FtpUtil.upLoadFile(AlipayConst.REMOTE_PATH, Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常", e);
                    return Rest.errorMsg("支付宝预下单失败!!!");
                }
                log.info("qrPath:" + qrPath);
                String qrUrl = FtpConsts.HTTP_PREFIX + targetFile.getName();
                Map<String, String> resultMap = Maps.newHashMap();
                resultMap.put("orderNo", String.valueOf(order.getOrderNo()));
                resultMap.put("qrUrl", qrUrl);
                return Rest.okData(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return Rest.errorMsg("支付宝预下单失败!!!");
            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return Rest.errorMsg("系统异常，预下单状态未知!!!");
            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return Rest.errorMsg("不支持的交易状态，交易返回异常!!!");
        }
    }

    @Override
    @ArgsNotEmpty
    public Rest aliCallback(Map<String, String> params) {
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = this.getOrder(orderNo);
        if (order == null) {
            return Rest.errorMsg("非商城的订单,回调忽略");
        }
        if (order.getStatus() >= OrderEnum.PAID.getCode()) {
            return Rest.errorMsg("支付宝重复调用");
        }
        if (AlipayConst.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            order.setPaymentTime(MyDateUtil.strToDateTime(params.get("gmt_payment")));
            order.setStatus(OrderEnum.PAID.getCode());
            this.updateById(order);
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        payInfoService.save(payInfo);

        return Rest.ok();
    }

    @Override
    @ArgsNotEmpty
    public boolean manageSendGoods(Long orderNo) {
        Order order = this.getOrder(orderNo);
        if (order != null) {
            if (order.getStatus() == OrderEnum.PAID.getCode()) {
                order.setStatus(OrderEnum.SHIPPED.getCode());
                order.setSendTime(LocalDateTime.now());
                this.updateById(order);
                return this.updateById(order);
            }
        }
        return false;
    }

    @ArgsNotEmpty
    @Override
    public Order getOrder(Long orderNo) {
        return this.getOne(this.lambdaQuery().eq(Order::getOrderNo, orderNo));
    }

    @Override
    @ArgsNotEmpty
    public boolean queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = this.getUserOrder(userId, orderNo);
        return MyBeanUtil.isRequired(order) && order.getStatus() >= OrderEnum.PAID.getCode();
    }
}

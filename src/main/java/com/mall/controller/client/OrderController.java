package com.mall.controller.client;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.mall.common.Rest;
import com.mall.common.annotation.UserLogin;
import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.common.consts.AlipayConst;
import com.mall.common.consts.UserEnum;
import com.mall.controller.BaseController;
import com.mall.entity.Order;
import com.mall.entity.User;
import com.mall.service.OrderService;
import com.mall.vo.in.MyPageIn;
import com.mall.vo.out.MyPageVo;
import com.mall.vo.out.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author lijun
 * @date 2020-06-17 16:58
 * @description 用户订单控制器
 * @error
 * @since version-1.0
 */
@Controller
@RequestMapping("/order/")
@UserLogin
public class OrderController extends BaseController {


    @Autowired
    private OrderService orderService;

    /**
     * 通过购物车id创建订单
     *
     * @param shippingId 购物车id
     * @return 订单对象
     */
    @RequestMapping("create")
    @ResponseBody
    @WebParamNotEmpty
    public Rest create(Integer shippingId) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        return Rest.okData(orderService.createOrder(user.getId(), shippingId));
    }

    /**
     * 取消订单
     *
     * @param orderNo 订单号
     * @return boolean
     */
    @RequestMapping("cancel")
    @ResponseBody
    @WebParamNotEmpty
    public Rest cancel(Long orderNo) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        return Rest.okData(orderService.cancel(user.getId(), orderNo));
    }

    /**
     * 获得购物车所有物品预览
     *
     * @return 订单vo
     */
    @RequestMapping("geOrderCartProduct")
    @ResponseBody
    public Rest getOrderCartProduct() {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        return Rest.okData(orderService.getOrderCartProduct(user.getId()));
    }

    /**
     * 订单详情
     *
     * @param orderNo 订单号
     * @return 订单详情
     */
    @RequestMapping("detail")
    @ResponseBody
    public Rest detail(Long orderNo) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        return Rest.okData(orderService.getUserOrderDetail(user.getId(), orderNo));
    }

    /**
     * 订单分页详情
     *
     * @param myPageIn 订单分页参数
     * @return 分页列表
     */
    @RequestMapping("list")
    @ResponseBody
    @WebParamNotEmpty
    public Rest list(MyPageIn myPageIn) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        Page<Order> orderPage = orderService.getPage(myPageIn);
        if (CollectionUtils.isEmpty(orderPage.getRecords())) {
            return Rest.okData(MyPageVo.getEmpty(myPageIn));
        }
        List<OrderVo> orderVos = orderService.assembleOrderVoList(orderPage.getRecords(), user.getId());
        return Rest.okData(MyPageVo.getInstance(orderVos, orderPage));
    }


    /**
     * 生成支付宝支付二维码
     *
     * @param orderNo 订单号
     * @return 支付参数
     */
    @RequestMapping("pay")
    @ResponseBody
    public Rest pay(Long orderNo) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        return orderService.pay(orderNo, user.getId());
    }

    /**
     * 支付回调
     *
     * @return
     */
    @RequestMapping("alipayCallback")
    @ResponseBody
    public Object alipayCallback() {
        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {

                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.

        params.remove("sign_type");
        try {
            boolean alipayRsaCheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            if (!alipayRsaCheckedV2) {
                return Rest.errorMsg("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常", e);
        }

        //todo 验证各种数据
        //
        Rest rest = orderService.aliCallback(params);
/*        if (Rest.isSuccess()) {
            return AlipayConst.RESPONSE_SUCCESS;
        }*/
        return AlipayConst.RESPONSE_FAILED;
    }

    /**
     * 产看订单状态
     *
     * @param orderNo 订单号
     * @return 订单状态
     */
    @RequestMapping("query_order_pay_status")
    @ResponseBody
    @WebParamNotEmpty
    public Rest queryOrderPayStatus(Long orderNo) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        return Rest.okData(orderService.queryOrderPayStatus(user.getId(), orderNo));
    }
}

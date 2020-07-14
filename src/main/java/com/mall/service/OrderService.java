package com.mall.service;

import com.mall.common.Rest;
import com.mall.entity.Order;
import com.mall.entity.OrderItem;
import com.mall.vo.out.OrderProductVo;
import com.mall.vo.out.OrderVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
public interface OrderService extends BaseService<Order> {
    /**
     * 通过用户购物车id创建订单
     *
     * @param id         用户id
     * @param shippingId 购物车id
     * @return 订单对象
     */
    OrderVo createOrder(Integer id, Integer shippingId);

    /**
     * 订单取消
     *
     * @param id      用户id
     * @param orderNo 订单号
     * @return boolean
     */
    boolean cancel(Integer id, Long orderNo);

    /**
     * 获得用户订单
     *
     * @param id      用户ID
     * @param orderNo 订单号
     * @return 订单
     */
    Order getUserOrder(Integer id, Long orderNo);


    /**
     * 获得购物车中商品
     *
     * @param id 用户id
     * @return 购物车商品
     */
    OrderProductVo getOrderCartProduct(Integer id);


    /**
     * 封住订单数据
     *
     * @param order         订单
     * @param orderItemList 订单集合
     * @return 订单数据
     */
    OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList);

    /**
     * 获得用户订单详情
     *
     * @param id      用户ID
     * @param orderNo 订单号
     * @return 订单详情
     */
    OrderVo getUserOrderDetail(Integer id, Long orderNo);

    /**
     * 订单号获得订单详情
     *
     * @param orderNo 订单号
     * @return 订单详情
     */
    OrderVo getOrderDetail(Long orderNo);

    /**
     * 订单集合数据封装对像
     *
     * @param orderList 订单集合
     * @param userId    用户id
     * @return 订单vo集合
     */
    List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId);


    /**
     * 获得订单详情
     *
     * @param orderNo 订单号
     * @return 订单
     */
    Order getOrder(Long orderNo);

    /**
     * 订单是否支付
     *
     * @param id      用户id
     * @param orderNo 订单号
     * @return boolean
     */
    boolean queryOrderPayStatus(Integer id, Long orderNo);

    /**
     * 支付宝支付接口
     *
     * @param orderNo 订单号
     * @param id      Userid
     * @return 支付结果集
     */
    Rest pay(Long orderNo, Integer id);

    /**
     * 支付宝回调接口
     *
     * @param params 回调参数集
     * @return 回调状态
     */
    Rest aliCallback(Map<String, String> params);

    /**
     *  订单发货
     * @param orderNo 订单号
     * @return 是否成功
     */
    boolean manageSendGoods(Long orderNo);
}

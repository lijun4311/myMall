package com.mall.service;

import com.mall.entity.OrderItem;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
public interface OrderItemService extends BaseService<OrderItem> {
    /**
     * 获得用户子订单
     *
     * @param orderNo 订单号
     * @param id      用户id
     * @return 子订单集合
     */
    List<OrderItem> getUserOrderItem(Long orderNo, Integer id);
}

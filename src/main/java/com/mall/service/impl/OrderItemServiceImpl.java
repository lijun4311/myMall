package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.annotation.ArgsNotEmpty;
import com.mall.entity.OrderItem;
import com.mall.mapper.OrderItemMapper;
import com.mall.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Override
    @ArgsNotEmpty
    public List<OrderItem> getUserOrderItem(Long orderNo, Integer id) {
        return this.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderNo, orderNo).eq(OrderItem::getUserId, id));
    }

    @Override
    @ArgsNotEmpty
    public List<OrderItem> getOrderItem(Long orderNo) {
        return this.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderNo, orderNo));
    }
}

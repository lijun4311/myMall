package com.mall.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.mall.common.Rest;
import com.mall.common.annotation.UserLogin;
import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.controller.BaseController;
import com.mall.entity.Order;
import com.mall.entity.OrderItem;
import com.mall.service.OrderItemService;
import com.mall.service.OrderService;
import com.mall.vo.in.MyPageIn;
import com.mall.vo.out.MyPageVo;
import com.mall.vo.out.OrderVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by geely
 */

@Controller
@RequestMapping("/manage/order")
@UserLogin
public class OrderManageController extends BaseController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @RequestMapping("list")
    @ResponseBody
    public Rest<MyPageVo> orderList(MyPageIn myPageIn) {
        Page<Order> pageData = orderService.getPage(myPageIn);
        List<OrderVo> orderVo = orderService.assembleOrderVoList(pageData.getRecords(), 0);
        return Rest.okData(MyPageVo.getInstance(orderVo, pageData));
    }

    @RequestMapping("detail")
    @ResponseBody
    public Rest<OrderVo> orderDetail(Long orderNo) {
        return Rest.okData(orderService.getOrderDetail(orderNo));
    }


    @RequestMapping("search")
    @ResponseBody
    public Rest<OrderVo> orderSearch(MyPageIn in,Long orderNo) {
        Order order = orderService.getOrder(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemService.getOrderItem(orderNo);
            OrderVo orderVo = orderService.assembleOrderVo(order, orderItemList);
            return Rest.okData(orderVo);

        }
        return Rest.errorMsg("未找到订单");
    }


    @RequestMapping("sendGoods")
    @ResponseBody
    @WebParamNotEmpty
    public Rest<String> orderSendGoods(Long orderNo) {
        return orderService.manageSendGoods(orderNo) ? Rest.okMsg("发货成功") : Rest.errorMsg("发货失败");
    }


}

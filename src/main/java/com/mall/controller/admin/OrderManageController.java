package com.mall.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.Rest;
import com.mall.common.annotation.UserLogin;
import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.controller.BaseController;
import com.mall.entity.Order;
import com.mall.service.OrderItemService;
import com.mall.service.OrderService;
import com.mall.vo.in.MyPageIn;
import com.mall.vo.out.MyPageVo;
import com.mall.vo.out.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
/**
 * @author lijun
 * @date 2020-07-14 19:39
 * @description 管理员订单控制器
 * @since version-1.0
 * @error
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
        Page<Order> pageData = orderService.getPage(myPageIn, null, Order.class);
        if (CollectionUtils.isEmpty(pageData.getRecords())) {
            return Rest.okData(MyPageVo.getEmpty(myPageIn));
        }
        List<OrderVo> orderVo = orderService.assembleOrderVoList(pageData.getRecords(), 0);
        return Rest.okData(MyPageVo.getInstance(orderVo, pageData));
    }

    @RequestMapping("detail")
    @ResponseBody
    public Rest<OrderVo> orderDetail(Long orderNo) {
        return Rest.okData(orderService.getOrderDetail(orderNo));
    }


    @RequestMapping("sendGoods")
    @ResponseBody
    @WebParamNotEmpty
    public Rest<String> orderSendGoods(Long orderNo) {
        return orderService.manageSendGoods(orderNo) ? Rest.okMsg("发货成功") : Rest.errorMsg("发货失败");
    }


}

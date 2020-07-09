package com.mall.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.Rest;
import com.mall.common.annotation.UserLogin;
import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.controller.BaseController;
import com.mall.entity.Shipping;
import com.mall.service.ShippingService;
import com.mall.vo.in.MyPageIn;
import com.mall.vo.out.MyPageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lijun
 * @date 2020-06-17 16:58
 * @description  用户购物地址控制器
 * @since version-1.0
 * @error
 */
@Controller
@RequestMapping("/shipping/")
@UserLogin
public class ShippingController extends BaseController {

    @Autowired
    private ShippingService shippingService;

    /**
     * 添加收货地址
     *
     * @param shipping 地址对象
     * @return rest
     */
    @RequestMapping("add")
    @ResponseBody
    @WebParamNotEmpty
    public Rest add(Shipping shipping) {
        return Rest.isSuccess(shippingService.save(shipping));
    }

    /**
     * 查询收货地址
     *
     * @param shippingId 收货地址id
     * @return 收货地址
     */
    @RequestMapping("select")
    @ResponseBody
    @WebParamNotEmpty
    public Rest<Shipping> select(Integer shippingId) {
        return Rest.okData(shippingService.getById(shippingId));
    }

    /**
     * 删除收货地址
     *
     * @param shippingId 收货地址id
     * @return rest
     */
    @RequestMapping("del")
    @ResponseBody
    @WebParamNotEmpty
    public Rest del(Integer shippingId) {

        return Rest.isSuccess(shippingService.removeById(shippingId));
    }

    /**
     * 更新地址对象
     *
     * @param shipping 收货地址对象
     * @return rest
     */
    @RequestMapping("update")
    @ResponseBody
    @WebParamNotEmpty
    public Rest update(Shipping shipping) {
        if (shipping.getId() <= 0) {
            return Rest.illegalParam();
        }
        return Rest.isSuccess(shippingService.updateById(shipping));
    }

    /**
     * 收货列表分页
     *
     * @param myPageIn 分页查询数据
     * @return 返回分页对象
     */
    @RequestMapping("list")
    @ResponseBody
    @WebParamNotEmpty
    public Rest<MyPageVo> list(MyPageIn myPageIn) {
        Page<Shipping> pageData = shippingService.getPage(myPageIn, null, Shipping.class);
        return Rest.okData(MyPageVo.getInstance(pageData));
    }
}

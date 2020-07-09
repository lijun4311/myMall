package com.mall.controller.backend;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall.common.Rest;
import com.mall.common.annotation.UserLogin;
import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.common.consts.CartConst;
import com.mall.common.consts.UserEnum;
import com.mall.controller.BaseController;
import com.mall.entity.Cart;
import com.mall.entity.User;
import com.mall.service.CartService;
import com.mall.vo.out.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Author lijun
 * @Date 2020-06-15 17:09
 * @Description 用户购物车控制器
 * @Since version-1.0
 */
@Controller
@RequestMapping("/cart")
@UserLogin
public class CartController extends BaseController {

    @Autowired
    private CartService cartService;

    /**
     * 获得购物车商品数量
     *
     * @return 购物车数量
     */
    @RequestMapping("productCount")
    @ResponseBody
    @UserLogin
    public Rest<Integer> getCartProductCount() {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        return Rest.okData(cartService.getMyCartCount(user.getId()));
    }

    /**
     * 获得用户购物车列表
     *
     * @return 购物车列表对象
     */
    @RequestMapping("list")
    @ResponseBody
    @UserLogin
    public Rest<CartVo> list() {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        CartVo cartVo = cartService.getCartVoList(user.getId());
        return Rest.okData(cartVo);
    }

    /**
     * 廷加购物车数量
     *
     * @param count     数量
     * @param productId 商品id
     * @return 购物车列表对象
     */
    @RequestMapping("add")
    @ResponseBody
    @UserLogin
    @WebParamNotEmpty
    public Rest<CartVo> add(Integer count, Integer productId) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        CartVo cartVo = cartService.add(user.getId(), productId, count);
        return Rest.okData(cartVo);
    }

    /**
     * 更新商品数量
     *
     * @param count     数量
     * @param productId 商品id
     * @return 购物车列表对象
     */
    @RequestMapping("update")
    @ResponseBody
    public Rest<CartVo> update(Integer count, Integer productId) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        Cart cart = cartService.getCartOne(user.getId(), productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        //汉子
        cartService.updateById(cart);
        return Rest.okData(cartService.getCartVoList(user.getId()));
    }

    /**
     * 删除购物车商品
     *
     * @param productIds 商品ids
     * @return 购物车列表对象
     */
    @RequestMapping("deleteProduct")
    @ResponseBody
    public Rest<CartVo> deleteProduct(String productIds) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        LambdaUpdateWrapper<Cart> luw = new LambdaUpdateWrapper<Cart>();
        luw.eq(Cart::getUserId, user.getId());
        luw.in(Cart::getProductId, productIds);
        cartService.remove(luw);
        return Rest.okData(cartService.getCartVoList(user.getId()));
    }


    /**
     * 购物车选中状态
     *
     * @return 购物车列表对象
     */
    @RequestMapping("select")
    @ResponseBody
    public Rest<CartVo> select(Integer productId, Integer checked) {
        User user = (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
        switch (checked) {
            case CartConst.CHECKED:
                checked = CartConst.CHECKED;
                break;
            case CartConst.UN_CHECKED:
                checked = CartConst.UN_CHECKED;
                break;
            default:
                return Rest.illegalParam();
        }
        cartService.upChecked(productId, user.getId(), checked);
        return Rest.okData(cartService.getCartVoList(user.getId()));
    }

}

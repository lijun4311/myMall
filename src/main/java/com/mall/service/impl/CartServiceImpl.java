package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.mall.common.annotation.ArgsNotEmpty;
import com.mall.common.consts.CartConst;
import com.mall.common.consts.propertiesconsts.FtpConsts;
import com.mall.entity.Cart;
import com.mall.entity.Product;
import com.mall.mapper.CartMapper;
import com.mall.service.CartService;
import com.mall.service.ProductService;
import com.mall.util.BigDecimalUtil;
import com.mall.util.MyBeanUtil;
import com.mall.vo.out.CartProductVo;
import com.mall.vo.out.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class CartServiceImpl extends BaseServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private ProductService productService;

    @Override
    @ArgsNotEmpty
    public CartVo getCartVoList(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = getCartList(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        for (Cart cartItem : cartList) {
            CartProductVo cartProductVo = new CartProductVo();
            cartProductVo.setId(cartItem.getId());
            cartProductVo.setUserId(userId);
            cartProductVo.setProductId(cartItem.getProductId());
            Product product = productService.getOne(new LambdaQueryWrapper<Product>().eq(Product::getId, cartItem.getProductId()));
            if (product != null) {
                cartProductVo.setProductMainImage(product.getMainImage());
                cartProductVo.setProductName(product.getName());
                cartProductVo.setProductSubtitle(product.getSubtitle());
                cartProductVo.setProductStatus(product.getStatus());
                cartProductVo.setProductPrice(product.getPrice());
                cartProductVo.setProductStock(product.getStock());
                //判断库存
                int buyLimitCount = 0;
                if (product.getStock() >= cartItem.getQuantity()) {
                    //库存充足的时候
                    buyLimitCount = cartItem.getQuantity();
                    cartProductVo.setLimitQuantity(CartConst.LIMIT_NUM_SUCCESS);
                } else {
                    buyLimitCount = product.getStock();
                    cartProductVo.setLimitQuantity(CartConst.LIMIT_NUM_FAIL);
                    //购物车中更新有效库存
                }
                LambdaUpdateWrapper<Cart> luw = new LambdaUpdateWrapper<>();
                //TODO 库存处理
                luw.eq(Cart::getId, cartItem.getId());
                luw.set(Cart::getQuantity, buyLimitCount);
                this.update(luw);
                cartProductVo.setQuantity(buyLimitCount);
                //计算总价
                cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                cartProductVo.setProductChecked(cartItem.getChecked());
            }
            if (cartItem.getChecked() == CartConst.CHECKED) {
                //如果已经勾选,增加到整个的购物车总价中
                cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
            }
            cartProductVoList.add(cartProductVo);
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(FtpConsts.HTTP_PREFIX);
        return cartVo;
    }


    @ArgsNotEmpty
    @Override
    public List<Cart> getCartList(Integer userId) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Cart::getUserId, userId);
        return this.list(lambdaQueryWrapper);
    }

    @ArgsNotEmpty
    @Override
    public boolean getAllCheckedStatus(Integer userId) {
        LambdaQueryWrapper<Cart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Cart::getUserId, userId);
        lqw.eq(Cart::getChecked, CartConst.UN_CHECKED);
        return this.count(lqw) == 0;
    }

    @Override
    @ArgsNotEmpty
    public CartVo add(Integer userId, Integer productId, Integer count) {
        Cart cart = getCartOne(userId, productId);
        if (cart == null) {
            //这个产品不在这个购物车里,需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(CartConst.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            this.save(cartItem);
        } else {
            //这个产品已经在购物车里了.
            //如果产品已存在,数量相加
            count = cart.getQuantity() + count;
            this.update(new LambdaUpdateWrapper<Cart>().set(Cart::getQuantity, count).eq(Cart::getId, cart.getId()));
        }
        return this.getCartVoList(userId);
    }

    @Override
    @ArgsNotEmpty
    public Cart getCartOne(Integer id, Integer productId) {
        return this.getOne(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, id).eq(Cart::getProductId, productId));
    }

    @Override
    @ArgsNotEmpty
    public List<Cart> selectCheckCarts(Integer id) {
        return this.list(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, id).eq(Cart::getChecked, CartConst.CHECKED));
    }

    @Override
    @ArgsNotEmpty
    public int getMyCartCount(Integer id) {
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, id);
        return this.count(queryWrapper);
    }

    @Override
    @ArgsNotEmpty("productId,checked")
    public void upChecked(Integer productId, Integer userId, int checked) {
        LambdaUpdateWrapper<Cart> luw = new LambdaUpdateWrapper<>();
        luw.eq(Cart::getUserId, userId);
        luw.eq(!MyBeanUtil.isRequired(productId), Cart::getProductId, productId);
        luw.set(Cart::getChecked, checked);
        this.update(luw);
    }

}

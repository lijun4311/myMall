package com.mall.service;

import com.mall.entity.Cart;
import com.mall.vo.out.CartVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
public interface CartService extends BaseService<Cart> {
    /**
     * 获得购物车列表
     *
     * @param userId 用户id
     * @return CartVo
     */
    CartVo getCartVoList(Integer userId);

    /**
     * 获得购物车物品集合
     *
     * @param userId 用户id
     * @return 购物车集合
     */
    List<Cart> getCartList(Integer userId);

    /**
     * 是否全选
     *
     * @param userId 用户id
     * @return boolean
     */
    boolean getAllCheckedStatus(Integer userId);

    /**
     * 添加购物车 对象
     *
     * @param id        用户id
     * @param productId 商品id
     * @param count     商品数量
     * @return 购物车对象
     */
    CartVo add(Integer id, Integer productId, Integer count);

    /**
     * 获得单个购物车对象
     *
     * @param id        用户id
     * @param productId 商品id
     * @return 购物车对象
     */
    Cart getCartOne(Integer id, Integer productId);

    /**
     * 获得选中购物车对象集合
     *
     * @param id 用户id
     * @return 购物车对象
     */
    List<Cart> selectCheckCarts(Integer id);

    /**
     * 获得购物车数量
     *
     * @param id 用户id
     * @return 购物车数量
     */
    int getMyCartCount(Integer id);

    /**
     * 购物车单选取消
     *
     * @param productId 商品id
     * @param userId    用户id
     * @param checked   选中状态
     */
    void upChecked(Integer productId, Integer userId, int checked);
}

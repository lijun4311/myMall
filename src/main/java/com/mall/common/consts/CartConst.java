package com.mall.common.consts;

/**
 * @author lijun
 * @date 2020-06-16 19:55
 * @description 购物车常量
 * @error
 * @since version-1.0
 */
public interface CartConst {
    /**
     * 即购物车选中状态
     */
    int CHECKED = 1;
    /**
     * 购物车中未选中状态
     */
    int UN_CHECKED = 0;
    /**
     * 限制数量失败
     */
    String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
    /**
     * 限制数量成功
     */
    String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";

}

package com.mall.vo.out;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lijun
 * @date 2020-06-30 15:56
 * @description 购物车商品封装对象
 * @since version-1.0
 * @error
 */
@Data
public class CartProductVo {



    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private String productName;
    private String productSubtitle;
    private String productMainImage;
    private BigDecimal productPrice;
    private Integer productStatus;
    private BigDecimal productTotalPrice;
    private Integer productStock;
    /**
     * 此商品是否勾选
     */
    private Integer productChecked;
    /**
     * 限制数量的一个返回结果
     */
    private String limitQuantity;
}

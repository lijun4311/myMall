package com.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
@TableName("mmall_cart")
@Data
public class Cart extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;


    private Integer userId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 是否选择,1=已勾选,0=未勾选
     */
    private Integer checked;
}

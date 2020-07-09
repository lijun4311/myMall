package com.mall.vo.out;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author lijun
 * @date 2020-06-30 19:10
 * @description  商品集合封装对象
 * @since version-1.0
 * @error
 */
@Data
public class ProductListVo {

    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private BigDecimal price;
    private Integer status;
    private String imageHost;
}

package com.mall.vo.out;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author lijun
 * @date 2020-06-30 15:59
 * @description  商品详情封装数据
 * @since version-1.0
 * @error
 */
@Data
public class ProductDetailVo {

    private Integer  id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private String subImages;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String createTime;
    private String updateTime;
    private String imageHost;
    private Integer parentCategoryId;
}

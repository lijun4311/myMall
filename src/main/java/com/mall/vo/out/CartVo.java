package com.mall.vo.out;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lijun
 * @date 2020-06-30 15:53
 * @description 购物车封装对象
 * @since version-1.0
 * @error
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice;
    /**
     * 是否已经都勾选
     */
    private Boolean allChecked;
    private String imageHost;
}

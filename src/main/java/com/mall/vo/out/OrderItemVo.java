package com.mall.vo.out;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author lijun
 * @date 2020-06-30 15:54
 * @description  子订单封装结果对象
 * @since version-1.0
 * @error
 */
@Data
public class OrderItemVo {

    private Long orderNo;

    private Integer productId;

    private String productName;
    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String createTime;

}

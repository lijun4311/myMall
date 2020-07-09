package com.mall.vo.out;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lijun
 * @date 2020-06-30 15:55
 * @description 订单商品封装对象
 * @error
 * @since version-1.0
 */
@Data
public class OrderProductVo {
    private List<OrderItemVo> orderItemVoList;
    private BigDecimal productTotalPrice;
    private String imageHost;


}

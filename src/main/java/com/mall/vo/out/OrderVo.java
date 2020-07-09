package com.mall.vo.out;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lijun
 * @date 2020-06-30 15:58
 * @description 订单封装对象
 * @since version-1.0
 * @error
 */
@Data
public class OrderVo {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;
    private Integer postage;

    private Integer status;


    private String statusDesc;

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    /**
     * 订单的明细
     */
    private List<OrderItemVo> orderItemVoList;

    private String imageHost;
    private Integer shippingId;
    private String receiverName;

    private ShippingVo shippingVo;


}

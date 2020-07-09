package com.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
@TableName("mmall_order")
@Data
public class Order extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 用户id
     */
    private Integer userId;

    private Integer shippingId;

    /**
     * 实际付款金额,单位是元,保留两位小数
     */
    private BigDecimal payment;

    /**
     * 支付类型,1-在线支付
     */
    private Integer paymentType;

    /**
     * 运费,单位是元
     */
    private Integer postage;

    /**
     * 订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
     */
    private Integer status;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    private LocalDateTime sendTime;

    /**
     * 交易完成时间
     */
    private LocalDateTime endTime;

    /**
     * 交易关闭时间
     */
    private LocalDateTime closeTime;
}

package com.mall.common.consts;

import com.mall.util.MyLogUtil;

/**
 * @author lijun
 * @date 2020-06-16 19:56
 * @description 订单枚举
 * @error
 * @since version-1.0
 */
public enum OrderEnum {
    //订单状态 code 状态值 value 状态描述
    CANCELED(0, "已取消"),
    NO_PAY(10, "未支付"),
    PAID(20, "已付款"),
    SHIPPED(40, "已发货"),
    ORDER_SUCCESS(50, "订单完成"),
    ORDER_CLOSE(60, "订单关闭");


    OrderEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private final String value;
    private final int code;

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public static String codeOf(int code) {
        for (OrderEnum orderStatusEnum : values()) {
            if (orderStatusEnum.getCode() == code) {
                return orderStatusEnum.value;
            }
        }
        MyLogUtil.log.error("OrderEnum 订单状态获取异常 {}",code);
        return "订单状态异常";
    }
}

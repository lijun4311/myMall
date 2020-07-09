package com.mall.common.consts;

import com.mall.util.MyLogUtil;
/**
 * @author lijun
 * @date 2020-06-30 15:58
 * @description  支付方式枚举
 * @since version-1.0
 * @error
 */
public enum PaymentTypeEnum {
    //支付方式 code 码 value 描述
    ONLINE_PAY(1, "在线支付");

    PaymentTypeEnum(int code, String value) {
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
        for (PaymentTypeEnum paymentTypeEnum : values()) {
            if (paymentTypeEnum.getCode() == code) {
                return paymentTypeEnum.value;
            }
        }
        MyLogUtil.log.error("PaymentTypeEnum 支付状态异常 {}",code);
        return "支付状态异常";
    }

}

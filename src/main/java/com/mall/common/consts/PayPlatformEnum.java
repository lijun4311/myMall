package com.mall.common.consts;

import com.mall.util.MyLogUtil;

/**
 * @author lijun
 * @date 2020-06-16 20:06
 * @description 支付平台枚举
 * @error
 * @since version-1.0
 */
public enum PayPlatformEnum {
    //支付平台类型 code 码 value 描述
    ALIPAY(1, "支付宝");

    PayPlatformEnum(int code, String value) {
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
        for (PayPlatformEnum platformEnum : values()) {
            if (platformEnum.getCode() == code) {
                return platformEnum.value;
            }
        }
        MyLogUtil.log.error("PayPlatformEnum 支付平台状态异常 {}",code);
        return "支付平台类型异常";
    }
}

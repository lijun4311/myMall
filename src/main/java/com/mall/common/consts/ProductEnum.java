package com.mall.common.consts;

import com.mall.util.MyLogUtil;

/**
 * @author lijun
 * @date 2020-06-16 20:08
 * @description 商品状态枚举
 * @error
 * @since version-1.0
 */
public enum ProductEnum {
    //商品状态 code 码 value 描述
    ON_SALE(1, "在线");
    private final String value;
    private final int code;

    ProductEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public static String codeOf(int code) {
        for (ProductEnum productEnum : values()) {
            if (productEnum.getCode() == code) {
                return productEnum.value;
            }
        }
        MyLogUtil.log.error("ProductEnum 商品状态异常 {}", code);
        return "商品状态异常";
    }
}

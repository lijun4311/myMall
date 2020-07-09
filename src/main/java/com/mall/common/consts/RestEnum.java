package com.mall.common.consts;

import com.mall.util.MyLogUtil;

/**
 * @Author lijun
 * @Date 2020-05-01 14:41
 * @Description 返回类型枚举
 * @Since version-1.0
 */
public enum RestEnum {
    //请求成功
    SUCCESS(200, "SUCCESS"),
    //请求错误
    ERROR(500, "ERROR"),
    //未登录
    NOLOGIN(800, "NOLOGIN"),
    //参数不合法
    ILLEGAL_PARAM(900, "ILLEGAL_PARAM");

    private final int code;
    private final String desc;


    RestEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String codeOf(int code) {
        for (RestEnum restEnum : values()) {
            if (restEnum.getCode() == code) {
                return restEnum.desc;
            }
        }
        MyLogUtil.log.error("RestEnum 错误码描述获取异常 {}", code);
        return "错误码描述获取异常";
    }

}

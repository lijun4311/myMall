package com.mall.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mall.common.consts.RestEnum;

import java.io.Serializable;

/**
 * @Author lijun
 * @Date 2020-05-19 19:30
 * @Description 网络传输响应实体类
 * @Since version-1.0
 * JsonInclude 保证序列化json的时候,如果是null的对象,key也会消失
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rest<T> implements Serializable {

    private final int status;
    private String msg;
    private T data;

    private Rest(int status) {
        this.status = status;
    }

    private Rest(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private Rest(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private Rest(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static <T> Rest<T> isSuccess(boolean success) {
        if (success) {
            return new Rest<>(RestEnum.SUCCESS.getCode());
        } else {
            return new Rest<>(RestEnum.ERROR.getCode(), RestEnum.ERROR.getDesc());
        }
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }


    public static <T> Rest<T> ok() {
        return new Rest<>(RestEnum.SUCCESS.getCode());
    }

    public static <T> Rest<T> okMsg(String msg) {
        return new Rest<>(RestEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Rest<T> okData(T data) {
        return new Rest<>(RestEnum.SUCCESS.getCode(), data);
    }

    public static <T> Rest<T> okDataMsg(String msg, T data) {
        return new Rest<>(RestEnum.SUCCESS.getCode(), msg, data);
    }


    public static <T> Rest<T> error() {
        return new Rest<>(RestEnum.ERROR.getCode(), RestEnum.ERROR.getDesc());
    }


    public static <T> Rest<T> illegalParam() {
        return new Rest<>(RestEnum.ILLEGAL_PARAM.getCode(), RestEnum.ILLEGAL_PARAM.getDesc());
    }

    public static <T> Rest<T> noLgoin() {
        return new Rest<>(RestEnum.NOLOGIN.getCode(), RestEnum.NOLOGIN.getDesc());
    }

    public static <T> Rest<T> errorMsg(String errorMessage) {
        return new Rest<>(RestEnum.ERROR.getCode(), errorMessage);
    }

    public static <T> Rest<T> errorDateMsg(String errorMessage, T data) {
        return new Rest<>(RestEnum.ERROR.getCode(), errorMessage, data);
    }

    public static <T> Rest<T> errorCodeMsg(int errorCode, String errorMessage) {
        return new Rest<>(errorCode, errorMessage);
    }


}

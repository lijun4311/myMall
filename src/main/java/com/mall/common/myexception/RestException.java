package com.mall.common.myexception;

/**
 * @author lijun
 * @date 2020-06-18 11:26
 * @description
 * @error
 * @since version-1.0
 */
public class RestException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    /**
     *  无参数的构造方法
     */
    public RestException() {
    }

    /**
     * 有参数的构造方法
     * @param msg 提示信息
     */
    public RestException(String msg) {
        // 把参数传递给Throwable的带String参数的构造方法
        super(msg);
    }

    public static RestException throwExcption(String msg) {
        return new RestException(msg);
    }
}

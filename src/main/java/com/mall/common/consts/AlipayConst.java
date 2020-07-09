package com.mall.common.consts;

/**
 * @author lijun
 * @date 2020-06-16 19:53
 * @description 支付宝sdk 常量接口
 * @error
 * @since version-1.0
 */
public interface AlipayConst {

    // String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    /**
     * 坏的连接
     */
    String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
    /**
     * 失败响应
     */
    String RESPONSE_SUCCESS = "success";
    /**
     * 成功响应
     */
    String RESPONSE_FAILED = "failed";
    /**
     * 支付图片路径
     */
    String REMOTE_PATH ="payImg";

}

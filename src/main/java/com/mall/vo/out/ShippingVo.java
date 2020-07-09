package com.mall.vo.out;

import lombok.Data;

/**
 * @author lijun
 * @date 2020-06-30 15:56
 * @description 购物地址对象封装
 * @since version-1.0
 * @error
 */
@Data
public class ShippingVo {
    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;


}

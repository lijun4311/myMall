package com.mall.common.annotation;

import com.mall.common.Rest;
import com.mall.common.consts.UserEnum;
import com.mall.entity.User;

import java.lang.annotation.*;

/**
 * @author lijun
 * @date 2020-06-16 18:58
 * @description 用户登录 用户对象{@link User}存入 requeest 中
 * 对象获取标示 {@link UserEnum#REQUEST_USER} 引用方法{@link com.mall.common.myinterceptor.LoginInterceptor}
 * @error 错误返回 {@link Rest#noLgoin()}
 * @since version-1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UserLogin {
}

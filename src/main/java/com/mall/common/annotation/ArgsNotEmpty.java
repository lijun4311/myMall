package com.mall.common.annotation;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.*;

/**
 * @author lijun
 * @date 2020-06-16 18:51
 * @description 参数不为空方法注解 {@link com.mall.common.aop.ParamAop#checkedNotEmptyBefore(JoinPoint)}
 * @error {@link IllegalArgumentException}
 * @since version-1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ArgsNotEmpty {
    String value() default "";
}

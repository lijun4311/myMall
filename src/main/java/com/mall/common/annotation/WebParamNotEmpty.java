package com.mall.common.annotation;

import com.mall.common.Rest;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.*;

/**
 * @author lijun
 * @date 2020-06-16 19:11
 * @description controller 参数是否为空效验 {@link com.mall.common.aop.ParamAop#checkWebParamsNotEmpty(ProceedingJoinPoint)}
 * @error 错误返回 {@link Rest#illegalParam()}
 * @target 方法 参数
 * @since version-1.0
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WebParamNotEmpty {

}

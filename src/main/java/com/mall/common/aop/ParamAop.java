package com.mall.common.aop;

import com.mall.common.Rest;
import com.mall.common.annotation.ArgsNotEmpty;
import com.mall.common.annotation.ArgsNotNull;
import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.util.MyBeanUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author lijun
 * @date 2020-06-16 19:21
 * @description StringAop 统一参数切面处理类
 * @error
 * @since version-1.0
 */
@Aspect
@Component
public class ParamAop {
    /**
     * {@link com.mall.common.annotation.ArgsNotNull} 参数不为null aop处理方法
     *
     * @param joinPoint 连接点
     * @throws IllegalArgumentException 指定的值为负值
     */
    @Before("@annotation(com.mall.common.annotation.ArgsNotNull)")
    public void checkedNotNullBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = signature.getParameterNames();
        ArgsNotNull argsNotNull = method.getAnnotation(ArgsNotNull.class);
        String fieldName = argsNotNull.value();
        int i = ArrayUtils.indexOf(parameterNames, fieldName);
        args = i > -1 ? ArrayUtils.remove(args, i) : args;
        for (Object o : args) {
            if (MyBeanUtil.isNull(o)) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * {@link com.mall.common.annotation.ArgsNotEmpty} 参数不为空 aop处理方法
     *
     * @param joinPoint 连接点
     * @throws IllegalArgumentException 指定的值为负值
     */
    @Before("@annotation(com.mall.common.annotation.ArgsNotEmpty)")
    public void checkedNotEmptyBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = signature.getParameterNames();
        ArgsNotEmpty argsNotEmpty = method.getAnnotation(ArgsNotEmpty.class);
        String[] fieldNames = argsNotEmpty.value().split(",");
        for (String name : fieldNames) {
            int i = ArrayUtils.indexOf(parameterNames, name);
            parameterNames = i > -1 ? ArrayUtils.remove(parameterNames, i) : parameterNames;
            args = i > -1 ? ArrayUtils.remove(args, i) : args;
        }
        if (MyBeanUtil.isRequired(args)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param proceedingJoinPoint 进行连接点
     * @return 方法结束返回对象
     * @throws Throwable 异常父类
     */
    @Around("execution(public * com.mall.controller..*.*(..))")
    public Object checkWebParamsNotEmpty(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();
        Method method = signature.getMethod();
        WebParamNotEmpty webParamNotEmpty = method.getAnnotation(WebParamNotEmpty.class);
        if (webParamNotEmpty != null) {
            if (MyBeanUtil.isRequired(args)) {
                return Rest.illegalParam();
            }
        } else {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                int paramIndex = ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof WebParamNotEmpty) {
                        Object paramValue = args[paramIndex];
                        if (MyBeanUtil.isRequired(paramValue)) {
                            return Rest.illegalParam();
                        }
                    }
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }
}

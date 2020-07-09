package com.mall.util.lambda.functional;

import java.io.Serializable;
import java.util.function.Function;
/**
 * @author lijun
 * @date 2020-06-30 19:09
 * @description  自定义序列化函数接口
 * @since version-1.0
 * @error
 */
@FunctionalInterface
public interface MyFunction<T, R> extends Function<T, R>, Serializable {

}

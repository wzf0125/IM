package org.quanta.im.annotation;

import java.lang.annotation.*;

/**
 * Description: 自定义权限拦截注解 通过传入权限值 在经过拦截器时会校验用户权限是否满足注解中预置的权限值
 * Param:
 * return:
 * Author: wzf mqh hzq zjx
 * Date: 2022/12/10
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Permission {
    int[] value();
}

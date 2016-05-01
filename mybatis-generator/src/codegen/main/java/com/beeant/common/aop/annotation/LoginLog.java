package com.beeant.common.aop.annotation;

import com.beeant.common.aop.enums.EnAppLog;

import java.lang.annotation.*;

/**
 * Created by Beeant on 2016/4/20.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginLog {
    EnAppLog code() default EnAppLog.LOGIN;
    String username();
}

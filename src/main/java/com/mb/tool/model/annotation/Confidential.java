package com.mb.tool.model.annotation;

import com.mb.tool.model.constant.LogConstants;

import java.lang.annotation.*;


@Inherited
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Confidential {
    int level() default LogConstants.CONFIDENTIAL_HIDE;
}

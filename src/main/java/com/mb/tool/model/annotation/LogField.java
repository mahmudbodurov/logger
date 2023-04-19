package com.mb.tool.model.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.mb.tool.model.constant.LogConstants.LOG_FIELD_TO_STRING;

@Retention(RetentionPolicy.RUNTIME)
public @interface LogField {
    int type() default LOG_FIELD_TO_STRING;
}

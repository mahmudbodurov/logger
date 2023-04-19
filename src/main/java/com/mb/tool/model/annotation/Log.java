package com.mb.tool.model.annotation;


import java.lang.annotation.*;

/**
 * Indicates that a method parameters will be logged
 *
 * <h3>Overview</h3>
 *
 * <pre class="code">
 *     &#064;Log
 *     public void doSomeStuff(String plainData,
 *                             &#064;LogField(type = LOG_FIELD_TO_STRING) Object callToStoString,
 *                             Object callAllField,
 *                             &#064;Confidential String hiddenData,
 *                             &#064;Confidential(level = LogConstants.CONFIDENTIAL_REMARK) String remarkedData) {
 *         System.out.println("plainData: " + plainData);
 *         System.out.println("hiddenData: " + hiddenData);
 *         System.out.println("remarkedData: " + remarkedData);
 *     }
 * </pre>
 *
 * @author Mahmud Bodurov
 * @see com.mb.tool.aspect.LogHandler
 * @see com.mb.tool.model.annotation.LogField
 * @see com.mb.tool.model.annotation.Confidential
 * @see com.mb.tool.model.constant.LogConstants
 */

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String suffix() default "ActionLog";
}

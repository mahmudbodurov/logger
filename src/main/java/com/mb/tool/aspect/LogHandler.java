package com.mb.tool.aspect;

import com.mb.tool.model.annotation.Confidential;
import com.mb.tool.model.annotation.Log;
import com.mb.tool.model.annotation.LogField;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.stereotype.Component;

import static com.mb.tool.model.constant.LogConstants.*;

@Component
@Aspect
public class LogHandler {

    @Pointcut("@annotation(logAspect)")
    public void callAt(Log logAspect) {
    }

    @Around(value = "callAt(logAspect)", argNames = "proceedingJoinPoint,logAspect")
    public Object around(ProceedingJoinPoint proceedingJoinPoint,
                         Log logAspect) throws Throwable {
        var signature = ((MethodSignature) proceedingJoinPoint.getSignature());
        var logger = LoggerFactory.getLogger(proceedingJoinPoint.getTarget().getClass().getCanonicalName());
        var builder = new StringBuilder();
        var parameters = signature.getMethod().getParameters();
        if (parameters != null)
            for (int i = 0; i < parameters.length; i++) {
                var confidentialMergedAnnotation = MergedAnnotations.from(parameters[i]).get(Confidential.class);
                if (confidentialMergedAnnotation.isPresent()) {
                    var confidence = confidentialMergedAnnotation.synthesize().level();
                    switch (confidence) {
                        case CONFIDENTIAL_HIDE:
                            continue;
                        case CONFIDENTIAL_REMARK:
                            builder.append(" ").append(parameters[i].getName()).append(":***");
                            break;
                    }
                } else {
                    var parentParameterName = parameters[i].getName();
                    var parentObject = proceedingJoinPoint.getArgs()[i];
                    boolean isJavaPackageClass = parentObject == null || Enum.class.isAssignableFrom(parentObject.getClass()) || parentObject.getClass().getPackageName().startsWith("java.") || parentObject.getClass().getPackageName().startsWith("org.spring");
                    if (isJavaPackageClass) {
                        builder.append(" ").append(parentParameterName).append(":").append(parentObject);
                    } else {
                        recursiveFieldRead(parameters[i].getAnnotation(LogField.class),
                                parentObject,
                                parentParameterName,
                                builder
                        );
                    }
                }
            }
        logger.info("{}.{}.start{}", logAspect.suffix(), signature.getName(), builder);
        Object response;
        try {
            response = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("{}.{}.error{}", logAspect.suffix(), signature.getName(), builder, throwable);
            throw throwable;
        }
        logger.info("{}.{}.success{}", logAspect.suffix(), signature.getName(), builder);
        return response;
    }

    private void recursiveFieldRead(LogField logField, Object parentObject, String parentParameterName, StringBuilder builder) throws IllegalAccessException {
        if (logField != null && LOG_FIELD_DEEP_READ == logField.type()) {
            for (var field : parentObject.getClass().getDeclaredFields()) {
                Object value = "";
                var annotation = field.getAnnotation(Confidential.class);
                if (annotation != null) {
                    var confidence = annotation.level();
                    switch (confidence) {
                        case CONFIDENTIAL_HIDE:
                            continue;
                        case CONFIDENTIAL_REMARK:
                            value = "***";
                    }
                } else {
                    field.setAccessible(true);
                    value = field.get(parentObject);
                    if (value != null) {
                        boolean isJavaPackageClass = Enum.class.isAssignableFrom(value.getClass()) || value.getClass().getPackageName().startsWith("java.") || value.getClass().getPackageName().startsWith("org.spring");
                        if (!isJavaPackageClass) {
                            var fieldAnnotation = field.getAnnotation(LogField.class);
                            recursiveFieldRead(fieldAnnotation,
                                    value,
                                    parentParameterName + "." + field.getName(),
                                    builder);
                            continue;
                        }
                    }
                }
                builder.append(" ")
                        .append(parentParameterName)
                        .append('.')
                        .append(field.getName())
                        .append(":")
                        .append(value);
            }
        } else {
            builder.append(" ").append(parentParameterName).append(":").append(parentObject);
        }
    }

}
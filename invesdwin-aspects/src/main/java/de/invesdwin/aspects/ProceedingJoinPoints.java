package de.invesdwin.aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.annotation.concurrent.Immutable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import de.invesdwin.util.lang.Reflections;

@Immutable
public final class ProceedingJoinPoints {

    private ProceedingJoinPoints() {}

    public static <T extends Annotation> T getAnnotation(final ProceedingJoinPoint pjp, final Class<T> annotationType) {
        final MethodSignature signature = (MethodSignature) pjp.getSignature();
        final Method method = signature.getMethod();
        return Reflections.getAnnotation(method, annotationType);
    }

    public static Method getMethod(final ProceedingJoinPoint pjp) {
        final MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getMethod();
    }

    public static Class<?> getDeclaringType(final ProceedingJoinPoint pjp) {
        return pjp.getSignature().getDeclaringType();
    }

}

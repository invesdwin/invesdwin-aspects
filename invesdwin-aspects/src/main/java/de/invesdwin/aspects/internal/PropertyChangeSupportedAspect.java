package de.invesdwin.aspects.internal;

import java.lang.reflect.Method;

import javax.annotation.concurrent.Immutable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import de.invesdwin.norva.beanpath.BeanPathReflections;
import de.invesdwin.util.bean.APropertyChangeSupported;

/**
 * Calls firePropertyChange after setters are called.
 * 
 * @author subes
 * 
 */
@Aspect
@Immutable
public class PropertyChangeSupportedAspect {

    @Around("execution(void *..*.set*(*)) && this(de.invesdwin.util.bean.APropertyChangeSupported)")
    public void setter(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
        final APropertyChangeSupported target = (APropertyChangeSupported) thisJoinPoint.getThis();
        if (target.hasListeners()) {
            final String propertyName = thisJoinPoint.getSignature().getName().substring("set".length());
            final String propertyNameSmall = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
            if (target.hasListeners(propertyNameSmall)) {
                final Object oldValue = getPropertyValue(target, propertyName);
                thisJoinPoint.proceed();
                final Object newValue = thisJoinPoint.getArgs()[0];
                target.firePropertyChange(propertyNameSmall, oldValue, newValue);
            } else {
                thisJoinPoint.proceed();
            }
        } else {
            thisJoinPoint.proceed();
        }
    }

    private Object getPropertyValue(final Object target, final String propertyName) {
        Object value = null;
        try {
            String getterName = BeanPathReflections.PROPERTY_GET_METHOD_PREFIX + propertyName;
            Method method;
            try {
                method = target.getClass().getMethod(getterName);
            } catch (final NoSuchMethodException e) {
                getterName = BeanPathReflections.PROPERTY_IS_METHOD_PREFIX + propertyName;
                method = target.getClass().getMethod(getterName);
            }

            if (method != null) {
                method.setAccessible(true);
                value = method.invoke(target);
            }

        } catch (final NoSuchMethodException e) {
            return null;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        return value;
    }
}

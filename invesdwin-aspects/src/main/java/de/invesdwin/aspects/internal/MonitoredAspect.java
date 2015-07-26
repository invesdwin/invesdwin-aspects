package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.Immutable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import de.invesdwin.aspects.ProceedingJoinPoints;
import de.invesdwin.aspects.annotation.Monitored;
import de.invesdwin.util.lang.Strings;

@Aspect
@Immutable
public class MonitoredAspect {

    @Around("execution(* *(..)) && @annotation(de.invesdwin.aspects.annotation.Monitored)")
    public Object monitored(final ProceedingJoinPoint pjp) throws Throwable {
        final String label;
        final Monitored annotation = ProceedingJoinPoints.getAnnotation(pjp, Monitored.class);
        if (annotation != null && Strings.isNotBlank(annotation.value())) {
            label = annotation.value();
        } else {
            label = getDefaultLabel(pjp);
        }

        final Monitor monitor = MonitorFactory.start(label);
        try {
            final Object ret = pjp.proceed();
            return ret;
        } finally {
            monitor.stop();
        }
    }

    private String getDefaultLabel(final ProceedingJoinPoint pjp) {
        final StringBuilder sb = new StringBuilder(pjp.getThis().getClass().getName());
        sb.append(".");
        sb.append(ProceedingJoinPoints.getMethod(pjp).getName());
        return sb.toString();
    }
}

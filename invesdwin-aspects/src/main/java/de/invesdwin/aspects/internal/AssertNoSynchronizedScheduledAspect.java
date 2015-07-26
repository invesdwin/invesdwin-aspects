package de.invesdwin.aspects.internal;

import java.lang.reflect.Method;

import javax.annotation.concurrent.ThreadSafe;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Scheduled;

import de.invesdwin.aspects.ProceedingJoinPoints;
import de.invesdwin.aspects.annotation.SkipParallelExecution;
import de.invesdwin.util.assertions.Assertions;
import de.invesdwin.util.lang.Reflections;

@ThreadSafe
@Aspect
public class AssertNoSynchronizedScheduledAspect {

    @Around("execution(* *(..)) && @annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object skipExecutionIfAlreadyRunning(final ProceedingJoinPoint pjp) throws Throwable {
        final Method method = ProceedingJoinPoints.getMethod(pjp);
        Assertions.assertThat(Reflections.isSynchronized(method))
                .as("@%s annotated method [%s] is synchronized. "
                        + "\nThis can cause the scheduler to not trigger future schedules properly because he is blocked on a monitor. "
                        + "\nPlease remove the synchronized modifier and use @%s instead to fix this.",
                        Scheduled.class.getSimpleName(), method.toString(), SkipParallelExecution.class.getSimpleName())
                .isFalse();
        return pjp.proceed();
    }
}

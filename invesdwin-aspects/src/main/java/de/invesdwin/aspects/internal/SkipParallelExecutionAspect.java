package de.invesdwin.aspects.internal;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.concurrent.ThreadSafe;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import de.invesdwin.util.assertions.Assertions;
import de.invesdwin.util.collections.loadingcache.ALoadingCache;

@ThreadSafe
@Aspect
public class SkipParallelExecutionAspect {

    private final ALoadingCache<String, AtomicBoolean> joinPoint_alreadyRunning = new ALoadingCache<String, AtomicBoolean>() {
        @Override
        protected AtomicBoolean loadValue(final String key) {
            Assertions.assertThat(key).startsWith("execution(void ");
            return new AtomicBoolean();
        }
    };

    @Around("execution(* *(..)) && (@annotation(de.invesdwin.aspects.annotation.SkipParallelExecution) || @annotation(org.springframework.scheduling.annotation.Scheduled))")
    public void skipExecutionIfAlreadyRunning(final ProceedingJoinPoint pjp) throws Throwable {
        final AtomicBoolean alreadyRunning = joinPoint_alreadyRunning.get(pjp.toString());
        if (!alreadyRunning.getAndSet(true)) {
            try {
                Assertions.assertThat(pjp.proceed()).isNull();
            } finally {
                Assertions.assertThat(alreadyRunning.getAndSet(false)).isTrue();
            }
        }
    }

}

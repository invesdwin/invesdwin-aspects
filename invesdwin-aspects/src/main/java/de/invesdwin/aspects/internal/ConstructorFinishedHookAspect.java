package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.ThreadSafe;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import de.invesdwin.aspects.hook.IConstructorFinishedHook;

@ThreadSafe
@Aspect
public class ConstructorFinishedHookAspect {

    @Around("target(de.invesdwin.aspects.hook.IConstructorFinishedHook) && execution(*.new(..))")
    public void afterConstructor(final ProceedingJoinPoint pjp) throws Throwable {
        pjp.proceed();
        //skip on exception in constructor
        if (pjp.getSignature().getDeclaringType().equals(pjp.getTarget().getClass())) {
            final IConstructorFinishedHook hook = (IConstructorFinishedHook) pjp.getTarget();
            hook.constructorFinished();
        }
    }
}
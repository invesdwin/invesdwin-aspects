package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.ThreadSafe;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import de.invesdwin.aspects.IConstructorFinishedHook;

@ThreadSafe
@Aspect
public class ConstructorFinishedHookAspect {
    @After("target(de.invesdwin.aspects.IConstructorFinishedHook) && execution(*.new(..))")
    public void afterConstructor(final JoinPoint jp) throws Throwable {
        if (jp.getSignature().getDeclaringType().equals(jp.getTarget().getClass())) {
            final IConstructorFinishedHook hook = (IConstructorFinishedHook) jp.getTarget();
            hook.onConstructorFinished();
        }
    }
}
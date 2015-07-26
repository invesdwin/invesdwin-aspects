package de.invesdwin.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This aspect ensures that a method is not executed if another thread is already running it. Instead of causing the
 * second thread to block until the first execution is done, with this annotation the method is simply not started
 * instead. This only works for methods with a void return type. This is useful for @Scheduled annotated methods to
 * prevent manual executions when the scheduler is already running this method.
 * 
 * This is a much better solution than making the method itself synchronized, because this can lead to the scheduler
 * being blocked on a monitor and thus not triggering anything anymore properly.
 * 
 * Methods with @Scheduled annotation automatically match the same aspect as this annotation, thus adding this one
 * aswell is not needed.
 * 
 * @author subes
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipParallelExecution {

}

package de.invesdwin.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Monitored {

    /**
     * The label for this Monitor. Default is <CassFQDN>.<MethodName>.
     */
    String value() default "";

}

package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.Immutable;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import de.invesdwin.instrument.DynamicInstrumentationLoader;

@Immutable
public final class InstrumentationTestInitializer {

    public static final InstrumentationTestInitializer INSTANCE = new InstrumentationTestInitializer();

    static {
        //initialize load time weaving
        DynamicInstrumentationLoader.waitForInitialized();
        final GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load(new ClassPathResource("/META-INF/ctx.spring.weaving.test.xml"));
        ctx.refresh();
    }

    private InstrumentationTestInitializer() {}

}

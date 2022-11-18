package de.invesdwin.aspects;

import javax.annotation.concurrent.Immutable;

import org.burningwave.core.assembler.StaticComponentContainer;

import de.invesdwin.instrument.DynamicInstrumentationLoader;

@Immutable
public final class InstrumentationTestInitializer {

    public static final InstrumentationTestInitializer INSTANCE = new InstrumentationTestInitializer();

    static {
        StaticComponentContainer.Modules.exportAllToAll();
        //initialize load time weaving
        DynamicInstrumentationLoader.waitForInitialized();
        org.assertj.core.api.Assertions.assertThat(DynamicInstrumentationLoader.initLoadTimeWeavingContext())
                .isNotNull();
    }

    private InstrumentationTestInitializer() {}

}

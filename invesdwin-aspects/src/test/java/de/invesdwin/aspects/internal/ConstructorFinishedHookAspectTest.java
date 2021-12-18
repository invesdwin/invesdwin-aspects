package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.NotThreadSafe;

import org.burningwave.core.assembler.StaticComponentContainer;
import org.junit.jupiter.api.Test;

import de.invesdwin.aspects.InstrumentationTestInitializer;
import de.invesdwin.util.assertions.Assertions;

@NotThreadSafe
public class ConstructorFinishedHookAspectTest {

    static {
        StaticComponentContainer.Modules.exportAllToAll();
        Assertions.assertThat(InstrumentationTestInitializer.INSTANCE).isNotNull();
    }

    @Test
    public void testConstructorFinishedHook() {
        new ExtendedConstructorFinishedBean().test();
    }

}

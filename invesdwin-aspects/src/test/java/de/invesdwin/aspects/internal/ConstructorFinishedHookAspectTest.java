package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.NotThreadSafe;

import org.junit.Test;

import de.invesdwin.util.assertions.Assertions;

@NotThreadSafe
public class ConstructorFinishedHookAspectTest {

    static {
        Assertions.assertThat(InstrumentationTestInitializer.INSTANCE).isNotNull();
    }

    @Test
    public void testConstructorFinishedHook() {
        new ExtendedConstructorFinishedBean().test();
    }

}

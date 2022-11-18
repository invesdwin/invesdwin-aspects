package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.ThreadSafe;

import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Scheduled;

import de.invesdwin.aspects.InstrumentationTestInitializer;
import de.invesdwin.aspects.annotation.SkipParallelExecution;
import de.invesdwin.util.assertions.Assertions;

@ThreadSafe
public class AssertNoSynchronizedScheduledAspectTest {

    static {
        Assertions.assertThat(InstrumentationTestInitializer.INSTANCE).isNotNull();
    }

    @Test
    public void testScheduled() throws InterruptedException {
        try {
            new ScheduledTestBean().scheduleSynchronized();
            Fail.fail("Exception expected");
        } catch (final Throwable e) {
            //CHECKSTYLE:OFF
            e.printStackTrace();
            //CHECKSTYLE:ON
            Assertions.assertThat(e.getMessage())
                    .contains("@" + Scheduled.class.getSimpleName())
                    .contains("@" + SkipParallelExecution.class.getSimpleName());
        }
    }

}

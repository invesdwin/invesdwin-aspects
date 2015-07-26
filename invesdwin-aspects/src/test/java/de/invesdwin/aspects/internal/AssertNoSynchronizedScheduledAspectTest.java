package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.ThreadSafe;

import org.assertj.core.api.Fail;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;

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
        } catch (final ComparisonFailure e) {
            Assertions.assertThat(e.getMessage())
                    .contains("@" + Scheduled.class.getSimpleName())
                    .contains("@" + SkipParallelExecution.class.getSimpleName());
        }
    }

    private class ScheduledTestBean {

        @Scheduled(fixedRate = Long.MAX_VALUE)
        public synchronized void scheduleSynchronized() {}

    }

}

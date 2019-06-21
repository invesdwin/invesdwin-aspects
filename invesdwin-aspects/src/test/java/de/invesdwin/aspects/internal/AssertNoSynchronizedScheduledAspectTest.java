package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.ThreadSafe;

import org.assertj.core.api.Fail;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;

import de.invesdwin.aspects.InstrumentationTestInitializer;
import de.invesdwin.aspects.annotation.SkipParallelExecution;
import de.invesdwin.util.assertions.Assertions;

@Ignore("will be fixed with aspectj 1.9.5")
@ThreadSafe
public class AssertNoSynchronizedScheduledAspectTest {

    static {
        Assertions.assertThat(InstrumentationTestInitializer.INSTANCE).isNotNull();
    }

    //wrong exception is currently being thrown by aspectj: https://bugs.eclipse.org/bugs/show_bug.cgi?id=547808
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

    public class ScheduledTestBean {

        @Scheduled(fixedRate = Long.MAX_VALUE)
        public synchronized void scheduleSynchronized() {}

    }

}

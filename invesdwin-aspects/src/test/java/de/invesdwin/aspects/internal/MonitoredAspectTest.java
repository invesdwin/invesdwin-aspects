package de.invesdwin.aspects.internal;

import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.ThreadSafe;

import org.junit.Test;

import com.jamonapi.MonitorFactory;

import de.invesdwin.aspects.annotation.Monitored;
import de.invesdwin.util.assertions.Assertions;

@ThreadSafe
public class MonitoredAspectTest {

    static {
        Assertions.assertThat(InstrumentationTestInitializer.INSTANCE).isNotNull();
    }

    @Test
    public void test() throws InterruptedException {
        final MonitoredAspectMethods m = new MonitoredAspectMethods();
        MonitorFactory.enable();
        final int loops = 10;
        for (int i = 0; i < loops; i++) {
            m.monitoredMethod();
        }
        final String report = MonitorFactory.getRootMonitor().getReport();
        System.out.println(report); //SUPPRESS CHECKSTYLE single line
        Assertions.assertThat(report).containsIgnoringCase(MonitoredAspectMethods.class.getName() + ".monitoredMethod");
        Assertions.assertThat(report).containsIgnoringCase(String.valueOf(loops) + ".0");
    }

    private class MonitoredAspectMethods {

        @Monitored
        private void monitoredMethod() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(5);
        }
    }

}

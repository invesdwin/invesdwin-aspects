package de.invesdwin.aspects;

import java.awt.EventQueue;

import javax.annotation.concurrent.NotThreadSafe;

import org.junit.jupiter.api.Test;

import de.invesdwin.util.time.Instant;
import io.netty.util.concurrent.FastThreadLocal;

@NotThreadSafe
public class EventDispatchThreadUtilTest {

    private static final int ITERATIONS = 10000000;

    private static final FastThreadLocal<Boolean> THREAD_LOCAL = new FastThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() throws Exception {
            //CHECKSTYLE:OFF
            return EventQueue.isDispatchThread();
            //CHECKSTYLE:ON
        }
    };

    @Test
    public void testPerformanceThreadLocal() {
        final Instant start = new Instant();
        for (int i = 0; i < ITERATIONS;) {
            if (!THREAD_LOCAL.get()) {
                i++;
            }
        }
        //CHECKSTYLE:OFF
        System.out.println("testPerformanceThreadLocal " + start);
        //CHECKSTYLE:ON
    }

    @Test
    public void testPerformanceDirect() {
        final Instant start = new Instant();
        for (int i = 0; i < ITERATIONS;) {
            //CHECKSTYLE:OFF
            if (!EventQueue.isDispatchThread()) {
                //CHECKSTYLE:ON
                i++;
            }
        }
        //CHECKSTYLE:OFF
        System.out.println("testPerformanceDirect " + start);
        //CHECKSTYLE:ON
    }

}

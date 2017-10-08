package de.invesdwin.aspects.internal;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.ThreadSafe;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.AsyncResult;

import de.invesdwin.aspects.annotation.EventDispatchThread;
import de.invesdwin.aspects.annotation.EventDispatchThread.InvocationType;
import de.invesdwin.util.assertions.Assertions;

@ThreadSafe
public class EventDispatchThreadAspectTest {

    private volatile boolean asyncMethodStarted;
    private volatile boolean asyncMethodFinished;

    static {
        Assertions.assertThat(InstrumentationTestInitializer.INSTANCE).isNotNull();
    }

    @Test
    public void futureInvokeLaterTest() throws InterruptedException, ExecutionException {
        final EdtAspectMethods m = new EdtAspectMethods();
        Assertions.assertThat(asyncMethodStarted).isFalse();
        Assertions.assertThat(asyncMethodFinished).isFalse();
        final Future<Boolean> future = m.futureInvokeLater();
        Assertions.assertThat(future.isDone()).isFalse();
        TimeUnit.MILLISECONDS.sleep(100);
        Assertions.assertThat(asyncMethodStarted).isTrue();
        Assertions.assertThat(asyncMethodFinished).isFalse();
        TimeUnit.SECONDS.sleep(1);
        Assertions.assertThat(asyncMethodStarted).isTrue();
        Assertions.assertThat(asyncMethodFinished).isTrue();
        Assertions.assertThat(future.isDone()).isTrue();
        Assertions.assertThat(future.get()).isTrue();
    }

    private class EdtAspectMethods {

        @EventDispatchThread(InvocationType.INVOKE_LATER)
        private Future<Boolean> futureInvokeLater() {
            asyncMethodStarted = true;
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
            asyncMethodFinished = true;
            return new AsyncResult<Boolean>(true);
        }
    }

}

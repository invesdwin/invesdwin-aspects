package de.invesdwin.aspects.internal;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.ThreadSafe;

import org.burningwave.core.assembler.StaticComponentContainer;
import org.junit.jupiter.api.Test;

import de.invesdwin.aspects.InstrumentationTestInitializer;
import de.invesdwin.util.assertions.Assertions;

@ThreadSafe
public class EventDispatchThreadAspectTest {

    static {
        StaticComponentContainer.Modules.exportAllToAll();
        Assertions.assertThat(InstrumentationTestInitializer.INSTANCE).isNotNull();
    }

    @Test
    public void futureInvokeLaterTest() throws InterruptedException, ExecutionException {
        final EdtAspectMethods m = new EdtAspectMethods();
        Assertions.assertThat(m.asyncMethodStarted).isFalse();
        Assertions.assertThat(m.asyncMethodFinished).isFalse();
        final Future<Boolean> future = m.futureInvokeLater();
        Assertions.assertThat(future.isDone()).isFalse();
        TimeUnit.MILLISECONDS.sleep(100);
        Assertions.assertThat(m.asyncMethodStarted).isTrue();
        Assertions.assertThat(m.asyncMethodFinished).isFalse();
        TimeUnit.SECONDS.sleep(1);
        Assertions.assertThat(m.asyncMethodStarted).isTrue();
        Assertions.assertThat(m.asyncMethodFinished).isTrue();
        Assertions.assertThat(future.isDone()).isTrue();
        Assertions.assertThat(future.get()).isTrue();
    }

}

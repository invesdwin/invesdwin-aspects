package de.invesdwin.aspects.internal;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.NotThreadSafe;

import org.springframework.scheduling.annotation.AsyncResult;

import de.invesdwin.aspects.annotation.EventDispatchThread;
import de.invesdwin.aspects.annotation.EventDispatchThread.InvocationType;

@NotThreadSafe
public class EdtAspectMethods {

    //CHECKSTYLE:OFF
    public volatile boolean asyncMethodStarted;
    public volatile boolean asyncMethodFinished;
    //CHECKSTYLE:ON

    @EventDispatchThread(InvocationType.INVOKE_LATER)
    public Future<Boolean> futureInvokeLater() {
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
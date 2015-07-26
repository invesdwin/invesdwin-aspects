package de.invesdwin.aspects;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.annotation.concurrent.Immutable;
import javax.swing.SwingWorker;

import de.invesdwin.util.concurrent.Futures;

/**
 * InterruptedExceptions are handled here transparently, because in GUI applications these normally shouldn't occur. If
 * they do, one can check with Thread.currentThread().isInterrupted() inside loops.
 * 
 */
@Immutable
public final class EventDispatchThreadUtil {

    private EventDispatchThreadUtil() {}

    public static <V, T> V executeAndWait(final SwingWorker<V, T> swingWorker) {
        swingWorker.execute();
        try {
            return Futures.get(swingWorker);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            return (V) null;
        }
    }

    public static <V, T> Future<V> execute(final SwingWorker<V, T> swingWorker) {
        swingWorker.execute();
        return swingWorker;
    }

    public static <V> V invokeAndWait(final Callable<V> callable) throws InterruptedException {
        final FutureTask<V> future = new FutureTask<V>(callable);
        invokeAndWait(future);
        return Futures.get(future);
    }

    public static <V> Future<V> invokeLaterIfNotInEDT(final Callable<V> callable) {
        final FutureTask<V> future = new FutureTask<V>(callable);
        invokeLaterIfNotInEDT(future);
        return future;
    }

    public static <V> Future<V> invokeLater(final Callable<V> callable) {
        final FutureTask<V> future = new FutureTask<V>(callable);
        invokeLater(future);
        return future;
    }

    public static void invokeAndWait(final Runnable runnable) throws InterruptedException {
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            try {
                //CHECKSTYLE:OFF must only be called by this class anyway
                EventQueue.invokeAndWait(runnable);
                //CHECKSTYLE:ON
            } catch (final InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void invokeLaterIfNotInEDT(final Runnable runnable) {
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            invokeLater(runnable);
        }
    }

    public static void invokeLater(final Runnable runnable) {
        //CHECKSTYLE:OFF must only be called by this class anyway
        EventQueue.invokeLater(runnable);
        //CHECKSTYLE:ON
    }

}

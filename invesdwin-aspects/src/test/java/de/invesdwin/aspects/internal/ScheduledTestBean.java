package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.Immutable;

import org.springframework.scheduling.annotation.Scheduled;

@Immutable
public class ScheduledTestBean {

    @Scheduled(fixedRate = Long.MAX_VALUE)
    public synchronized void scheduleSynchronized() {
    }

}
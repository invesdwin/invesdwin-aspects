package de.invesdwin.aspects;

import javax.annotation.concurrent.Immutable;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import de.invesdwin.aspects.internal.AssertNoSynchronizedScheduledAspectTest;
import de.invesdwin.aspects.internal.ConstructorFinishedHookAspectTest;
import de.invesdwin.aspects.internal.EventDispatchThreadAspectTest;
import de.invesdwin.aspects.internal.MemoryMeasurementTest;
import de.invesdwin.aspects.internal.PropertyChangeSupportedAspectTest;
import de.invesdwin.aspects.internal.PropertyChangeSupportedDirtyTrackerTest;

@Suite
@SelectClasses({ AssertNoSynchronizedScheduledAspectTest.class, EventDispatchThreadUtilTest.class,
        ConstructorFinishedHookAspectTest.class, EventDispatchThreadAspectTest.class, MemoryMeasurementTest.class,
        PropertyChangeSupportedAspectTest.class, PropertyChangeSupportedDirtyTrackerTest.class })
@Immutable
public class AspectsTestSuite {

}
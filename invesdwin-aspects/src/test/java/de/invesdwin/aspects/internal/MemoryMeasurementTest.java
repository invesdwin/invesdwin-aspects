package de.invesdwin.aspects.internal;

import java.lang.instrument.Instrumentation;

import javax.annotation.concurrent.NotThreadSafe;

import org.github.jamm.MemoryMeter;
import org.junit.Test;

import de.invesdwin.instrument.DynamicInstrumentationReflections;
import de.invesdwin.util.assertions.Assertions;
import de.invesdwin.util.math.decimal.Decimal;

@NotThreadSafe
public class MemoryMeasurementTest {

    static {
        Assertions.assertThat(InstrumentationTestInitializer.INSTANCE).isNotNull();
        final Instrumentation instrumentation = DynamicInstrumentationReflections.getInstrumentation();
        MemoryMeter.premain(null, instrumentation);
    }

    @Test
    public void testDecimalHeapSize() {
        final Decimal decimal = new Decimal("5");
        final MemoryMeter meter = new MemoryMeter();
        final long size = meter.measureDeep(decimal);
        //CHECKSTYLE:OFF
        System.out.println("without Digits: " + size);
        //CHECKSTYLE:ON
        decimal.getDecimalDigits();
        final long largerSize = meter.measureDeep(decimal);
        //CHECKSTYLE:OFF
        System.out.println("with Digits: " + largerSize);
        //CHECKSTYLE:ON
        Assertions.assertThat(largerSize).isGreaterThanOrEqualTo(size);
        decimal.isZero();
        decimal.isPositive();
        decimal.getWholeNumberDigits();
        decimal.getDigits();
        final long sameSize = meter.measureDeep(decimal);
        //CHECKSTYLE:OFF
        System.out.println("with additional info: " + sameSize);
        //CHECKSTYLE:ON
        Assertions.assertThat(sameSize).isEqualTo(largerSize);

        Assertions.assertThat(size).isEqualTo(24);
        Assertions.assertThat(sameSize).isEqualTo(24);
    }

}

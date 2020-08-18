package de.invesdwin.aspects.internal;

import java.lang.instrument.Instrumentation;
import java.util.BitSet;

import javax.annotation.concurrent.NotThreadSafe;

import org.github.jamm.MemoryMeter;
import org.junit.Test;
import org.roaringbitmap.RoaringBitmap;

import de.invesdwin.aspects.InstrumentationTestInitializer;
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

        final Double doublee = new Double(5D);
        final long doubleSize = meter.measureDeep(doublee);

        Assertions.assertThat(size).isEqualTo(doubleSize);
    }

    @Test
    public void testBooleanVsDoubleArraySize() {
        final int size = 1000000;
        final boolean[] bool = new boolean[size];
        final double[] doubl = new double[size];
        final BitSet bitSet = new BitSet(size);
        final RoaringBitmap roaringBitmap = new RoaringBitmap();
        for (int i = 0; i < size; i++) {
            final boolean value = i % 2 == 0;
            bool[i] = value;
            doubl[i] = i % 2;
            if (value) {
                bitSet.set(i);
                roaringBitmap.add(i);
            }
        }

        final MemoryMeter meter = new MemoryMeter();
        final double boolSize = meter.measureDeep(bool);
        final double doublSize = meter.measureDeep(doubl);
        final double bitSetSize = meter.measureDeep(bitSet);
        roaringBitmap.runOptimize();
        roaringBitmap.trim();
        for (int i = 0; i < bool.length; i++) {
            Assertions.assertThat(roaringBitmap.contains(i)).as("i: " + i).isEqualTo(bool[i]);
        }
        final double roaringBitmapSize = meter.measureDeep(roaringBitmap);

        //CHECKSTYLE:OFF
        System.out.println("booleanVsDouble: " + boolSize / 10 + " / " + doublSize / 10 + " = " + boolSize / doublSize
                + " or " + doublSize / boolSize);
        System.out.println("bitSetVsDouble: " + bitSetSize / 10 + " / " + doublSize / 10 + " = "
                + bitSetSize / doublSize + " or " + doublSize / bitSetSize);
        System.out.println("roaringBitmapVsDouble: " + roaringBitmapSize / 10 + " / " + doublSize / 10 + " = "
                + roaringBitmapSize / doublSize + " or " + doublSize / roaringBitmapSize);
        //CHECKSTYLE:ON
    }

}

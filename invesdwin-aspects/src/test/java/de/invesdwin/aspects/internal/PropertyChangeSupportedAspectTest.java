package de.invesdwin.aspects.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.invesdwin.aspects.InstrumentationTestInitializer;
import de.invesdwin.util.assertions.Assertions;
import de.invesdwin.util.bean.AValueObject;
import de.invesdwin.util.lang.Strings;

@ThreadSafe
public class PropertyChangeSupportedAspectTest {

    private boolean propertyChanged;

    static {
        Assertions.assertThat(InstrumentationTestInitializer.INSTANCE).isNotNull();
    }

    @Test
    public void testDeepClone() {
        final CloneableVO vo = new CloneableVO();
        vo.setValue(5);
        vo.setMutableValue(new MutableInt(5));
        final CloneableVO voClone = (CloneableVO) vo.clone();
        Assertions.assertThat(vo).isNotSameAs(voClone);
        //FST does not clone immutable values
        Assertions.assertThat(vo.getValue()).isSameAs(voClone.getValue());
        Assertions.assertThat(vo.getValue()).isEqualTo(voClone.getValue());
        //only mutable ones
        Assertions.assertThat(vo.getMutableValue()).isNotSameAs(voClone.getMutableValue());
        Assertions.assertThat(vo.getMutableValue()).isEqualTo(voClone.getMutableValue());
    }

    @Test
    public void testShallowClone() {
        final CloneableClass vo = new CloneableClass();
        final CloneableVO value = new CloneableVO();
        value.setValue(5);
        vo.setValue(value);
        final CloneableClass voClone = (CloneableClass) vo.clone();
        Assertions.assertThat(vo).isNotSameAs(voClone);
        Assertions.assertThat(vo.getValue()).isSameAs(voClone.getValue());
        Assertions.assertThat(vo.getValue()).isEqualTo(voClone.getValue());
        Assertions.assertThat(vo.getValue().getValue()).isSameAs(voClone.getValue().getValue());
        Assertions.assertThat(vo.getValue().getValue()).isEqualTo(voClone.getValue().getValue());
    }

    @Test
    public void testMergeFrom() {
        final Integer value = 5;
        final CloneableVO vo = new CloneableVO();
        vo.setValue(value);
        final CloneableVO newVo = new CloneableVO();
        newVo.mergeFrom(vo);
        Assertions.assertThat(value).isEqualTo(newVo.getValue());
        Assertions.assertThat(vo.getValue()).isEqualTo(newVo.getValue());

        newVo.setValue(null);
        vo.mergeFrom(newVo);
        Assertions.assertThat(newVo.getValue()).isNull();
        Assertions.assertThat(vo.getValue()).as("null values are not getting ignored!").isNotNull();

        final CloneableClass clazz = new CloneableClass();
        clazz.setOtherValue(1);
        vo.mergeFrom(clazz);
    }

    @Test
    public void testPropertyChangeSupport() {
        final CloneableVO vo = new CloneableVO();

        final PropertyChangeListener pcl = Mockito.mock(PropertyChangeListener.class);
        vo.addPropertyChangeListener(pcl);
        vo.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                Assertions.assertThat(evt.getPropertyName()).isEqualTo("value");
                Assertions.assertThat(evt.getOldValue()).isNull();
                Assertions.assertThat(evt.getNewValue()).isEqualTo(5);
                Assertions.assertThat(evt.getSource()).isSameAs(vo);
                System.out.println(Strings.asStringReflective(evt)); //SUPPRESS CHECKSTYLE single line
                propertyChanged = true;
            }
        });
        vo.setValue(5);
        Mockito.verify(pcl, Mockito.only()).propertyChange((PropertyChangeEvent) Mockito.any());
        Assertions.assertThat(propertyChanged).isTrue();
    }

    public static class CloneableVO extends AValueObject {
        private static final long serialVersionUID = 1L;

        private Integer value;

        private MutableInt mutableValue;

        public Integer getValue() {
            return value;
        }

        public void setValue(final Integer value) {
            this.value = value;
        }

        public MutableInt getMutableValue() {
            return mutableValue;
        }

        public void setMutableValue(final MutableInt mutableValue) {
            this.mutableValue = mutableValue;
        }

    }

    public static class CloneableClass implements Cloneable {
        private CloneableVO value;
        private Integer otherValue;

        public CloneableVO getValue() {
            return value;
        }

        public void setValue(final CloneableVO value) {
            this.value = value;
        }

        public Integer getOtherValue() {
            return otherValue;
        }

        public void setOtherValue(final Integer otherValue) {
            this.otherValue = otherValue;
        }

        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (final CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

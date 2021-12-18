package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class CloneableClass implements Cloneable {
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
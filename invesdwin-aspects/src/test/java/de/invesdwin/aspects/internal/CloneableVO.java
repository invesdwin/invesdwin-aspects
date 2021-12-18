package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.lang3.mutable.MutableInt;

import de.invesdwin.util.bean.AValueObject;

@NotThreadSafe
public class CloneableVO extends AValueObject {
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
package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.NotThreadSafe;

import de.invesdwin.util.bean.AValueObject;

@NotThreadSafe
public class OuterVo extends AValueObject {
    private static final long serialVersionUID = 1L;
    private InnerVO inner = new InnerVO();
    private Integer otherValue;

    public InnerVO getInner() {
        return inner;
    }

    public void setInner(final InnerVO value) {
        this.inner = value;
    }

    public Integer getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(final Integer otherValue) {
        this.otherValue = otherValue;
    }

}
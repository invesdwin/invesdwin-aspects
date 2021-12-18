package de.invesdwin.aspects.internal;

import javax.annotation.concurrent.NotThreadSafe;

import de.invesdwin.util.bean.AValueObject;

@NotThreadSafe
public class InnerVO extends AValueObject {
    private static final long serialVersionUID = 1L;

    private Integer value;
    private InnerInnerVO inner = new InnerInnerVO();

    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    public InnerInnerVO getInner() {
        return inner;
    }

    public void setInner(final InnerInnerVO inner) {
        this.inner = inner;
    }
}
package net.lilterror11;

public class ConditionalValue<A> {
    public final boolean has;
    public final A value;

    public ConditionalValue(A value) {
        this.has = true;
        this.value = value;
    }

    public ConditionalValue() {
        this.has = false;
        this.value = null;
    }
}

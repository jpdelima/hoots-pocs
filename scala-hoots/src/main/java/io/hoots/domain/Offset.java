package io.hoots.domain;

/**
 * Created by rwadowski on 13.04.17.
 */
public class Offset {

    private final int v;

    public Offset(int v) {
        this.v = v;
    }

    public long value() {
        return this.v;
    }

    @Override
    public boolean equals(Object object) {
        if(null == object) {
            return false;
        }

        if(this == object) {
            return true;
        }

        if(!(object instanceof Offset)) {
            return false;
        }

        final Offset that = (Offset) object;

        return that.v == this.v;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.v;
        return result;
    }
}

package io.hoots.domain.hash;

/**
 * Created by rwadowski on 13.04.17.
 */
public class Hash {

    private final Long v;

    public Hash(Long v) {
        this.v = v;
    }

    public Long value() {
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

        if(!(object instanceof Hash)) {
            return false;
        }

        final Hash that = (Hash) object;
        return that.v.equals(this.v);
    }

    @Override
    public int hashCode() {
        return this.v.hashCode();
    }
}

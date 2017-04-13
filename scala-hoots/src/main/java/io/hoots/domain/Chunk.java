package io.hoots.domain;

/**
 * Created by rwadowski on 13.04.17.
 */
public class Chunk {

    private final int no;

    public Chunk(int no) {
        this.no = no;
    }

    public int number() {
        return this.no;
    }

    @Override
    public boolean equals(Object object) {
        if(null == object) {
            return false;
        }

        if(this == object) {
            return true;
        }

        if(!(object instanceof Chunk)) {
            return false;
        }

        final Chunk that = (Chunk) object;
        return that.no == this.no;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.no;
        return result;
    }
}

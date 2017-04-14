package io.hoots.fingerprint.domain;

/**
 * Created by rwadowski on 13.04.17.
 */
public class Chunk {

    private final int no;
    private final int size;

    public Chunk(int no, int size) {
        this.no = no;
        this.size = size;
    }

    public int number() {
        return this.no;
    }

    public int getSize() {
        return this.size;
    }

    public float getByte() {
        return this.size * this.no;
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

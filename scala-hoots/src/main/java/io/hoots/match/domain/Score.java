package io.hoots.match.domain;

/**
 * Created by rwadowski on 13.04.17.
 */
public class Score {

    private int rank;

    public Score() {
        this(0);
    }

    public Score(int initialRank) {
        this.rank = initialRank;
    }

    public void add(int v) {
        this.rank = this.rank + v;
    }

    public int getRank() {
        return this.rank;
    }

    @Override
    public String toString() {
        return "Score(" + this.rank + ")";
    }
}

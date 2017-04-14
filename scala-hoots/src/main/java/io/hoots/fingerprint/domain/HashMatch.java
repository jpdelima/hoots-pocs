package io.hoots.fingerprint.domain;

import java.util.List;

/**
 * Created by rwadowski on 13.04.17.
 */
public class HashMatch {

    private final Hash hash;
    private final List<Point> samplePoints;
    private final List<Point> points;

    public HashMatch(Hash hash, List<Point> samplePoints, List<Point> points) {
        this.hash = hash;
        this.samplePoints = samplePoints;
        this.points = points;
    }

    public Hash getHash() {
        return this.hash;
    }

    public List<Point> getSamplePoints() {
        return this.samplePoints;
    }

    public List<Point> getPoints() {
        return points;
    }
}

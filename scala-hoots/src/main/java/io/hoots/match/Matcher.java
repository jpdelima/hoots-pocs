package io.hoots.match;

import io.hoots.domain.Item;
import io.hoots.domain.Point;
import io.hoots.domain.hash.Hash;
import io.hoots.domain.hash.HashMatch;
import io.hoots.domain.score.Score;

import java.util.*;

/**
 * Created by rwadowski on 07.04.17.
 */
public class Matcher {

    private final Map<Hash, List<Point>> hashes;

    public Matcher() {
        this(new HashMap<>());
    }

    public Matcher(Map<Hash, List<Point>> hashes) {
        this.hashes = hashes;
    }

    public void update(Map<Hash, List<Point>> h) {
        final Set<Hash> keys = h.keySet();
        for(Hash k : keys) {
            if(hashes.containsKey(k)) {
                final List<Point> points = hashes.get(k);
                final List<Point> newPoints = h.get(k);
                points.addAll(newPoints);
                hashes.put(k, points);
            } else {
                hashes.put(k, h.get(k));
            }
        }
    }

    public List<HashMatch> matchSmaple(Map<Hash, List<Point>> sample) {
        final List<HashMatch> matches = new ArrayList<>();
        final Set<Hash> sampleHashes = sample.keySet();
        for(Hash sh : sampleHashes) {
            if(!hashes.containsKey(sh)) {
                continue;
            }
            final List<Point> samplePoints = sample.get(sh);
            final List<Point> points = hashes.get(sh);
            final HashMatch m = new HashMatch(sh, samplePoints, points);
            matches.add(m);
        }
        return matches;
    }
}

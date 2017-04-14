package io.hoots.match;

import io.hoots.fingerprint.domain.Point;
import io.hoots.fingerprint.domain.Hash;
import io.hoots.fingerprint.domain.HashMatch;
import io.hoots.fingerprint.domain.FingerPrint;
import io.hoots.match.domain.Matches;

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

    public void update(FingerPrint print) {
        final Map<Hash, List<Point>> printHashes = print.getPrint();
        final Set<Hash> keys = printHashes.keySet();
        for(Hash k : keys) {
            if(hashes.containsKey(k)) {
                final List<Point> points = hashes.get(k);
                final List<Point> newPoints = printHashes.get(k);
                points.addAll(newPoints);
                hashes.put(k, points);
            } else {
                hashes.put(k, printHashes.get(k));
            }
        }
    }

    public Matches process(FingerPrint print) {
        final Map<Hash, List<Point>> sample = print.getPrint();
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
        return new Matches(matches, print.getInputFormat());
    }
}

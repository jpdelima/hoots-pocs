package io.hoots.match;

import io.hoots.input.domain.Item;
import io.hoots.match.domain.MatchResult;
import io.hoots.fingerprint.domain.Point;
import io.hoots.fingerprint.domain.HashMatch;
import io.hoots.match.domain.Matches;
import io.hoots.match.domain.Score;

import java.util.*;

/**
 * Created by rwadowski on 14.04.17.
 */
public class BasicAnalyzer implements MatchAnalyzer {

    @Override
    public MatchResult process(Matches m) {
        final List<HashMatch> matches = m.getMatchList();
        final Map<Item, Score> scores = new HashMap<>();
        final Map<Item, Point> matchPoints = new HashMap<>();
        for(HashMatch match : matches) {
            final Map<Item, List<Point>> grouped = group(match.getPoints());
            final Set<Item> items = grouped.keySet();
            final List<Integer> sampleOffsets = normalize(match.getSamplePoints());
            for(Item item : items) {
                final List<Point> o = grouped.get(item);
                final List<Integer> offs = normalize(o);
                offs.retainAll(sampleOffsets);
                final int score = offs.size();
                matchPoints.put(item, o.get(0));
                Score s;
                if(scores.containsKey(item)) {
                    s = scores.get(item);
                } else {
                    s = new Score();
                }
                s.add(score);
                scores.put(item, s);
            }
        }
        return new MatchResult(scores, m.getInputFormat(), matchPoints);
    }

    private List<Integer> normalize(List<Point> offsets) {
        final List<Integer> norm = new LinkedList<>();
        if(offsets.isEmpty()) {
            return norm;
        }
        final Integer head = offsets.get(0).getChunk().number();
        for(Point o : offsets) {
            norm.add(o.getChunk().number() - head);
        }
        return norm;
    }

    private Map<Item, List<Point>> group(List<Point> points) {
        final Map<Item, List<Point>> map = new HashMap<>();
        for(Point p : points) {
            if(map.containsKey(p.getItem())) {
                final List<Point> offsets = map.get(p.getItem());
                offsets.add(p);
            } else {
                final List<Point> offsets = new LinkedList<>();
                offsets.add(p);
                map.put(p.getItem(), offsets);
            }
        }
        final Collection<List<Point>> values = map.values();
        for(List<Point> l : values) {
            l.sort(Comparator.comparingInt(p -> p.getChunk().number()));
        }
        return map;
    }
}

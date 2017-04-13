package io.hoots.domain.score;

import io.hoots.domain.Item;
import io.hoots.domain.Point;
import io.hoots.domain.hash.HashMatch;

import java.util.*;

/**
 * Created by rwadowski on 14.04.17.
 */
public class BasicScoreCalculator implements ScoreCalculator {

    @Override
    public Map<Item, Score> calculate(List<HashMatch> matches) {
        final Map<Item, Score> scores = new HashMap<>();
        for(HashMatch match : matches) {
            final Map<Item, List<Integer>> grouped = group(match.getPoints());
            final Set<Item> items = grouped.keySet();
            final List<Integer> sampleOffsets = normalize(offsets(match.getSamplePoints()));
            for(Item item : items) {
                final List<Integer> o = grouped.get(item);
                final List<Integer> offs = normalize(o);
                offs.retainAll(sampleOffsets);
                final int score = offs.size();
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
        return scores;
    }

    private List<Integer> offsets(List<Point> points) {
        final List<Integer> ls = new LinkedList<>();
        if(points.isEmpty()) {
            return ls;
        }
        points.sort(Comparator.comparingInt(p -> p.getChunk().number()));
        for(Point p : points) {
            ls.add(p.getChunk().number());
        }
        return ls;
    }

    private List<Integer> normalize(List<Integer> offsets) {
        final List<Integer> norm = new LinkedList<>();
        if(offsets.isEmpty()) {
            return norm;
        }
        final Integer head = offsets.get(0);
        for(Integer o : offsets) {
            norm.add(o - head);
        }
        return norm;
    }

    private Map<Item, List<Integer>> group(List<Point> points) {
        final Map<Item, List<Integer>> map = new HashMap<>();
        for(Point p : points) {
            if(map.containsKey(p.getItem())) {
                final List<Integer> offsets = map.get(p.getItem());
                offsets.add(p.getChunk().number());
            } else {
                final List<Integer> offsets = new LinkedList<>();
                offsets.add(p.getChunk().number());
                map.put(p.getItem(), offsets);
            }
        }
        final Collection<List<Integer>> values = map.values();
        for(List<Integer> l : values) {
            l.sort(Comparator.comparingInt(i -> i));
        }
        return map;
    }
}

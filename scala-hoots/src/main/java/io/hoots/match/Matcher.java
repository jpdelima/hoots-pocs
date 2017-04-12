package io.hoots.match;

import io.hoots.domain.Point;

import java.util.*;

/**
 * Created by rwadowski on 07.04.17.
 */
public class Matcher {

    private final Map<Long, List<Point>> hashes;

    public Matcher() {
        this(new HashMap<>());
    }

    public Matcher(Map<Long, List<Point>> hashes) {
        this.hashes = hashes;
    }

    public void update(Map<Long, List<Point>> h) {
        final Set<Long> keys = h.keySet();
        for(Long k : keys) {
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

    public Map<UUID, Long> check(Map<Long, List<Point>> sample) {
        final Map<UUID, Long> scores = new HashMap<>();
        final Set<Long> sampleHashes = sample.keySet();
        for(Long sh : sampleHashes) {
            final List<Point> samplePoints = sample.get(sh);
            if(!hashes.containsKey(sh)) {
                continue;
            }
            final List<Point> points = hashes.get(sh);
            final Map<UUID, List<Integer>> map = group(points);
            final Set<UUID> ids = map.keySet();
            final List<Integer> sampleOffsets = normalize(offsets(samplePoints));
            for(UUID id : ids) {
                final List<Integer> o = map.get(id);
                final List<Integer> offs = normalize(o);
                offs.retainAll(sampleOffsets);
                final long score = offs.size();
                long s = 0;
                if(scores.containsKey(id)) {
                    s = scores.get(id);
                }
                scores.put(id, s + score);
            }
        }
        return scores;
    }

    private Map<UUID, List<Integer>> group(List<Point> points) {
        final Map<UUID, List<Integer>> map = new HashMap<>();
        for(Point p : points) {
            if(map.containsKey(p.getId())) {
                final List<Integer> offsets = map.get(p.getId());
                offsets.add(p.getTime());
            } else {
                final List<Integer> offsets = new LinkedList<>();
                offsets.add(p.getTime());
                map.put(p.getId(), offsets);
            }
        }
        final Collection<List<Integer>> values = map.values();
        for(List<Integer> l : values) {
            l.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1 - o2;
                }
            });
        }
        return map;
    }

    private List<Integer> offsets(List<Point> points) {
        final List<Integer> ls = new LinkedList<>();
        if(points.isEmpty()) {
            return ls;
        }
        points.sort((Point o1, Point o2) -> o1.getTime() - o2.getTime());
        for(Point p : points) {
            ls.add(p.getTime());
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
}

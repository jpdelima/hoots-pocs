package io.hoots.match.domain;

import io.hoots.fingerprint.domain.Chunk;
import io.hoots.fingerprint.domain.Point;
import io.hoots.input.domain.Item;

import javax.sound.sampled.AudioFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by rwadowski on 14.04.17.
 */
public class MatchResult {

    private final Map<Item, Score> scores;
    private final AudioFormat inputFormat;
    private final Map<Item, Point> matchPoints;

    public MatchResult(Map<Item, Score> scores, AudioFormat inputFormat, Map<Item, Point> matchPoints) {
        this.scores = scores;
        this.inputFormat = inputFormat;
        this.matchPoints = matchPoints;
    }

    public Map<Item, Score> getScores() {
        return this.scores;
    }

    public AudioFormat getInputFormat() {
        return this.inputFormat;
    }

    public Map<Item, Point> getMatchPoints() {
        return this.matchPoints;
    }

    public Map<Item, Float> getMatchTimestamps() {
        final Set<Item> items = matchPoints.keySet();
        final Map<Item, Float> timestamps = new HashMap<>(matchPoints.size());
        final int sampleSizeInBytes = inputFormat.getSampleSizeInBits() / 8;
        for(Item item : items) {
            final Chunk pointChunk = matchPoints.get(item).getChunk();
            final float ts = pointChunk.getByte() / (inputFormat.getSampleRate() * sampleSizeInBytes);
            timestamps.put(item, ts);
        }
        return timestamps;
    }
}

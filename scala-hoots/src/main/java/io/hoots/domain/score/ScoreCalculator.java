package io.hoots.domain.score;

import io.hoots.domain.Item;
import io.hoots.domain.hash.HashMatch;

import java.util.List;
import java.util.Map;

/**
 * Created by rwadowski on 13.04.17.
 */
public interface ScoreCalculator {

    public Map<Item, Score> calculate(List<HashMatch> matches);
}

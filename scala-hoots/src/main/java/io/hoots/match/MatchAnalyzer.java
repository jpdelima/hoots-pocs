package io.hoots.match;

import io.hoots.match.domain.MatchResult;
import io.hoots.match.domain.Matches;

/**
 * Created by rwadowski on 13.04.17.
 */
public interface MatchAnalyzer {

    MatchResult process(Matches matches);
}

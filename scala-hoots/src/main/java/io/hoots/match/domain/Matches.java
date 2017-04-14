package io.hoots.match.domain;

import io.hoots.fingerprint.domain.HashMatch;

import javax.sound.sampled.AudioFormat;
import java.util.List;

/**
 * Created by rwadowski on 14.04.17.
 */
public class Matches {

    private final List<HashMatch> matchList;
    private final AudioFormat inputFormat;

    public Matches(List<HashMatch> matchList, AudioFormat inputFormat) {
        this.matchList = matchList;
        this.inputFormat = inputFormat;
    }

    public List<HashMatch> getMatchList() {
        return this.matchList;
    }

    public AudioFormat getInputFormat() {
        return this.inputFormat;
    }
}

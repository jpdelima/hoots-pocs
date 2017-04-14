package io.hoots.fingerprint.domain;

import io.hoots.input.domain.Item;

import javax.sound.sampled.AudioFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by rwadowski on 14.04.17.
 */
public class FingerPrint {

    private final Map<Hash, List<Point>> print;
    private final Item item;
    private final AudioFormat inputFormat;

    public FingerPrint(Item item, Map<Hash, List<Point>> print, AudioFormat inputFormat) {
        this.item = item;
        this.print = print;
        this.inputFormat = inputFormat;
    }

    public Map<Hash, List<Point>> getPrint() {
        return this.print;
    }

    public Item getItem() {
        return this.item;
    }

    public AudioFormat getInputFormat() {
        return this.inputFormat;
    }
}

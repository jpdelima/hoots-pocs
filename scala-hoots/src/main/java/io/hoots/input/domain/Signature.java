package io.hoots.input.domain;

import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayOutputStream;

/**
 * Created by rwadowski on 14.04.17.
 */
public class Signature {

    private final Item item;
    private final ByteArrayOutputStream stream;
    private final AudioFormat inputFormat;

    public Signature(Item item, ByteArrayOutputStream stream, AudioFormat inputFormat) {
        this.item = item;
        this.stream = stream;
        this.inputFormat = inputFormat;
    }

    public Item getItem() {
        return this.item;
    }

    public ByteArrayOutputStream getStream() {
        return this.stream;
    }

    public AudioFormat getInputFormat() {
        return this.inputFormat;
    }
}

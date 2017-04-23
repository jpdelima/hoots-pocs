package io.hoots.signature;

import javax.sound.sampled.AudioFormat;

/**
 * Created by rwadowski on 14.04.17.
 */
public class WaveFileReaderDefaults {

    public static final int sampleRate = 44100;
    public static final int sampleSizeInBits = 8;
    public static final int channels = 1; //mono
    public static final boolean signed = true;
    public static final boolean bigEndian = true;
    public static final AudioFormat outputFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    public static final int BUFFER_SIZE = 1024;

    public static final AudioFormat.Encoding decodedEncoding = AudioFormat.Encoding.PCM_SIGNED;
    public static final int decodedSampleSizeInBits = 16;
    public static final boolean decodedBigEndian = false;

    private WaveFileReaderDefaults() {}
}
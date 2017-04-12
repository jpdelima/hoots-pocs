package io.hoots.fingerprint;

import io.hoots.FFT;
import io.hoots.domain.Complex;
import io.hoots.domain.Point;
import org.tritonus.sampled.convert.PCM2PCMConversionProvider;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;

/**
 * Created by rwadowski on 06.04.17.
 */
public class FingerPrinter {

    private final PCM2PCMConversionProvider conversionProvider = new PCM2PCMConversionProvider();
    private final int UPPER_LIMIT = 300;
    private final int LOWER_LIMIT = 40;
    private final int[] RANGE = new int[] { LOWER_LIMIT, 80, 120, 180, UPPER_LIMIT };

    public FingerPrinter() {}

    public Map<Long, List<Point>> exec(File ff, UUID id) {
        final ByteArrayOutputStream s = bytesFromFile(ff);
        return makeSpectrum(s, id);
    }


    private Map<Long, List<Point>> makeSpectrum(ByteArrayOutputStream out, UUID id) {
        final byte audio[] = out.toByteArray();
        final int totalSize = audio.length;
        final int amountPossible = totalSize / 4096;

        // When turning into frequency domain we'll need complex numbers:
        final Complex[][] results = new Complex[amountPossible][];

        // For all the chunks:
        for(int times = 0; times < amountPossible; times++) {
            final Complex[] complex = new Complex[4096];
            for(int i = 0; i < 4096; i++) {
                // Put the time domain data into a complex number with imaginary
                // part as 0:
                complex[i] = new Complex(audio[(times * 4096) + i], 0);
            }
            // Perform FFT analysis on the chunk:
            results[times] = FFT.fft(complex);
        }
        return determineKeyPoints(results, id);
    }


    private ByteArrayOutputStream bytesFromFile(File file) {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                    baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
                    false);

            AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
            AudioInputStream outDin = conversionProvider.getAudioInputStream(getFormat(), din);
            byte[] buffer = new byte[1024];
            int counter = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while((counter = outDin.read(buffer, 0, buffer.length)) != -1) {
                if(counter > 0) {
                    out.write(buffer, 0, counter);
                }
            }
            in.close();
            din.close();
            outDin.close();
            out.close();
            return out;
        } catch (Exception e) {
            return null;
        }
    }

    private AudioFormat getFormat() {
        float sampleRate = 44100;
        int sampleSizeInBits = 8;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    // Find out in which range
    private int getIndex(int freq) {
        int i = 0;
        while (RANGE[i] < freq)
            i++;
        return i;
    }

    private Map<Long, List<Point>> determineKeyPoints(Complex[][] results, UUID id) {
        final Map<Long, List<Point>> hashMap = new HashMap<>();

        final double highscores[][] = new double[results.length][RANGE.length];
        for (int i = 0; i < results.length; i++) {
            for (int j = 0; j < RANGE.length; j++) {
                highscores[i][j] = 0;
            }
        }

        long points[][] = new long[results.length][RANGE.length];
        for (int i = 0; i < results.length; i++) {
            for (int j = 0; j < RANGE.length; j++) {
                points[i][j] = 0;
            }
        }

        for(int t = 0; t < results.length; t++) {
            for(int freq = LOWER_LIMIT; freq < UPPER_LIMIT; freq++) {
                // Get the magnitude:
                double mag = Math.log(results[t][freq].abs() + 1);

                // Find out which range we are in:
                int index = getIndex(freq);

                // Save the highest magnitude and corresponding frequency:
                if(mag > highscores[t][index]) {
                    highscores[t][index] = mag;
                    points[t][index] = freq;
                }
            }

            long h = hash(points[t][0], points[t][1], points[t][2], points[t][3]);

            List<Point> listPoints = hashMap.get(h);
            if(listPoints == null) {
                listPoints = new ArrayList<>();
                Point point = new Point(id, t);
                listPoints.add(point);
                hashMap.put(h, listPoints);
            } else {
                Point point = new Point(id, t);
                listPoints.add(point);
            }
        }
        return hashMap;
    }

    private static final int FUZ_FACTOR = 3;

    private long hash(long p1, long p2, long p3, long p4) {
        return (p4 - (p4 % FUZ_FACTOR)) * 100000000 + (p3 - (p3 % FUZ_FACTOR))
                * 100000 + (p2 - (p2 % FUZ_FACTOR)) * 100
                + (p1 - (p1 % FUZ_FACTOR));
    }
}

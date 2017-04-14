package io.hoots.fingerprint;

import io.hoots.fft.domain.Complex;
import io.hoots.fingerprint.domain.Chunk;
import io.hoots.input.domain.Item;
import io.hoots.fingerprint.domain.Point;
import io.hoots.fingerprint.domain.Hash;
import io.hoots.fft.FFT;
import io.hoots.fingerprint.domain.FingerPrint;
import io.hoots.input.domain.Signature;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rwadowski on 06.04.17.
 */
public class FingerPrinter {

    private final int UPPER_LIMIT = 300;
    private final int LOWER_LIMIT = 40;
    private final int CHUNK_SIZE = 4096;

    private final int[] RANGE = new int[] { LOWER_LIMIT, 80, 120, 180, UPPER_LIMIT };

    public FingerPrinter() {}

    public FingerPrint process(Signature signature) {
        final Item item = signature.getItem();
        final Map<Hash, List<Point>> spectrum = makeSpectrum(signature.getStream(), item);
        return new FingerPrint(item, spectrum, signature.getInputFormat());
    }

    private Map<Hash, List<Point>> makeSpectrum(ByteArrayOutputStream out, Item item) {
        final byte audio[] = out.toByteArray();
        final int totalSize = audio.length;
        final int amountPossible = totalSize / CHUNK_SIZE;

        // When turning into frequency domain we'll need complex numbers:
        final Complex[][] results = new Complex[amountPossible][];

        // For all the chunks:
        for(int chunkNo = 0; chunkNo < amountPossible; chunkNo++) {
            final Complex[] complex = new Complex[CHUNK_SIZE];
            for(int i = 0; i < CHUNK_SIZE; i++) {
                // Put the time domain data into a complex number with imaginary
                // part as 0:
                complex[i] = new Complex(audio[(chunkNo * CHUNK_SIZE) + i], 0);
            }
            // Perform FFT analysis on the chunk:
            results[chunkNo] = FFT.fft(complex);
        }
        return determineKeyPoints(results, item);
    }

    // Find out in which range
    private int getIndex(int freq) {
        int i = 0;
        while (RANGE[i] < freq)
            i++;
        return i;
    }

    private Map<Hash, List<Point>> determineKeyPoints(Complex[][] results, Item item) {
        final Map<Hash, List<Point>> hashMap = new HashMap<>();

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
            final Hash hash = new Hash(h);
            List<Point> listPoints = hashMap.get(hash);
            if(listPoints == null) {
                listPoints = new ArrayList<>();
                Point point = new Point(item, new Chunk(t, CHUNK_SIZE));
                listPoints.add(point);
                hashMap.put(hash, listPoints);
            } else {
                Point point = new Point(item, new Chunk(t, CHUNK_SIZE));
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

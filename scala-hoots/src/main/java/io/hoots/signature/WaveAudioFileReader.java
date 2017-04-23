package io.hoots.signature;

import io.hoots.input.domain.Item;
import io.hoots.input.domain.Signature;
import org.tritonus.sampled.convert.PCM2PCMConversionProvider;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by rwadowski on 14.04.17.
 */
public class WaveAudioFileReader implements AudioFileReader {

    private final AudioFormat outputFormat;
    private final PCM2PCMConversionProvider conversionProvider = new PCM2PCMConversionProvider();

    public WaveAudioFileReader(AudioFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public WaveAudioFileReader() {
        this(WaveFileReaderDefaults.outputFormat);
    }

    @Override
    public Signature streamFromFile(File file) {
        try {
            final AudioInputStream in = AudioSystem.getAudioInputStream(file);
            final AudioFormat baseFormat = in.getFormat();
            final AudioFormat decodedFormat = decodedFormat(baseFormat);

            final AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
            final AudioInputStream outDin = conversionProvider.getAudioInputStream(outputFormat, din);
            final byte[] buffer = new byte[WaveFileReaderDefaults.BUFFER_SIZE];
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
            return new Signature(new Item(file.getName()), out, decodedFormat);
        } catch (Exception e) {
            return null;
        }
    }

    private AudioFormat decodedFormat(AudioFormat format) {
        AudioFormat decodedFormat = new AudioFormat(
                WaveFileReaderDefaults.decodedEncoding,
                format.getSampleRate(),
                WaveFileReaderDefaults.decodedSampleSizeInBits,
                format.getChannels(),
                format.getChannels() * 2,
                format.getSampleRate(),
                WaveFileReaderDefaults.decodedBigEndian);
        return decodedFormat;
    }
}
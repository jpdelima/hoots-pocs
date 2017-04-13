package io.hoots.input;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by rwadowski on 14.04.17.
 */
public interface AudioFileReader {

    public ByteArrayOutputStream streamFromFile(File file);
}

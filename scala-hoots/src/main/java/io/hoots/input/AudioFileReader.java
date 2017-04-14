package io.hoots.input;

import io.hoots.input.domain.Signature;

import java.io.File;

/**
 * Created by rwadowski on 14.04.17.
 */
public interface AudioFileReader {

    Signature streamFromFile(File file);
}

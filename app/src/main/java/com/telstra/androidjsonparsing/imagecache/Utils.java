package com.telstra.androidjsonparsing.imagecache;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for supporting the file operations
 */
public class Utils {

    /**
     *  Copy stream data bytes to output stream
     * @param is input stream object
     * @param os output stream object
     */
    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }
}
package com.redhat.lightblue.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public final class Utils {

    private Utils() {
    }

    public static final String loadResource(InputStream is) throws IOException {
        return loadResource(new InputStreamReader(is, Charset.defaultCharset()));
    }

    public static final String loadResource(Reader inReader) throws IOException {
        StringBuilder buff = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(inReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                buff.append(line).append("\n");
            }
        }

        return buff.toString();
    }

}

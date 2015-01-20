package com.redhat.lightblue.client.http.testing.doubles;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FakeServletOutputStream extends ServletOutputStream {
    private final OutputStream outputStream;

    public FakeServletOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public String toString() {
        return outputStream.toString();
    }
}

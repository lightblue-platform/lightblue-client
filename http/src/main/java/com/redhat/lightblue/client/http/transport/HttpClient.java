package com.redhat.lightblue.client.http.transport;

import com.redhat.lightblue.client.request.LightblueRequest;

import java.io.Closeable;
import java.io.IOException;

public interface HttpClient extends Closeable {
    String executeRequest(LightblueRequest request, String baseUri) throws IOException;
}

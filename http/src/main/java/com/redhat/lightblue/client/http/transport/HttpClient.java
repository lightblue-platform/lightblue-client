package com.redhat.lightblue.client.http.transport;

import com.redhat.lightblue.client.request.LightblueRequest;

import java.io.Closeable;
import java.io.IOException;

public interface HttpClient extends Closeable {
    // TODO: Do we need to able to examine headers or status code of response?
    String executeRequest(LightblueRequest request, String baseUri) throws IOException;
}

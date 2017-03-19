package com.redhat.lightblue.client.http.transport;

import java.io.Closeable;

import com.redhat.lightblue.client.http.LightblueHttpClientException;
import com.redhat.lightblue.client.request.LightblueRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface HttpTransport extends Closeable {
    HttpResponse executeRequest(LightblueRequest request, String baseUri, ObjectMapper mapper)
            throws LightblueHttpClientException;
}

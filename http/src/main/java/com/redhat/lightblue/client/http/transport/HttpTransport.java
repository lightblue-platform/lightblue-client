package com.redhat.lightblue.client.http.transport;

import java.io.Closeable;
import java.io.InputStream;

import com.redhat.lightblue.client.http.LightblueHttpClientException;
import com.redhat.lightblue.client.request.LightblueRequest;

public interface HttpTransport extends Closeable {

    HttpResponse executeRequest(LightblueRequest request, String baseUri) throws LightblueHttpClientException;
    InputStream executeRequestGetStream(LightblueRequest request, String baseUri) throws LightblueHttpClientException;

}

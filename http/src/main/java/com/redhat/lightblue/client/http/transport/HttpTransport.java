package com.redhat.lightblue.client.http.transport;

import java.io.Closeable;

import com.redhat.lightblue.client.http.LightblueHttpClientException;
import com.redhat.lightblue.client.request.LightblueRequest;

public interface HttpTransport extends Closeable {

    Response executeRequest(LightblueRequest request, String baseUri) throws LightblueHttpClientException;

}

package com.redhat.lightblue.client.http.request;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;

public class LightblueHttpDataRequest extends AbstractLightblueHttpRequest implements LightblueHttpRequest {

    private final LightblueRequest request;

    public LightblueHttpDataRequest(LightblueRequest request) {
        this.request = request;
    }

    @Override
    public HttpRequestBase getRestRequest(String baseServiceURI) {
        if (request instanceof AbstractLightblueDataRequest) {
            AbstractLightblueDataRequest dataRequest = (AbstractLightblueDataRequest) request;
            switch (dataRequest.getOperation()) {
                case DELETE:
                    return getHttpPost(dataRequest.getRestURI(baseServiceURI), dataRequest.getBody());
                case FIND:
                    return getHttpPost(dataRequest.getRestURI(baseServiceURI), dataRequest.getBody());
                case INSERT:
                    return getHttpPut(dataRequest.getRestURI(baseServiceURI), dataRequest.getBody());
                case SAVE:
                    return getHttpPost(dataRequest.getRestURI(baseServiceURI), dataRequest.getBody());
                case UPDATE:
                    return getHttpPost(dataRequest.getRestURI(baseServiceURI), dataRequest.getBody());
                default:
                    throw new UnsupportedOperationException("Unknown Operation type: " + request.getOperationPathParam());
            }
        }
        else {
            throw new UnsupportedOperationException("Request type is not supported: " + request.getClass());
        }
    }

}

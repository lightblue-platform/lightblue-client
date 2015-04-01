package com.redhat.lightblue.client.http.request;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.request.data.DataSaveRequest;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class LightblueHttpDataRequest extends AbstractLightblueHttpRequest implements LightblueHttpRequest {

    private final AbstractLightblueDataRequest request;

    public LightblueHttpDataRequest(AbstractLightblueDataRequest request) {
        this.request = request;
    }

    @Override
    public HttpRequestBase getRestRequest(String baseServiceURI) {
        HttpRequestBase httpRequest = null;

        if (request instanceof DataDeleteRequest) {
            return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
        } else if (request instanceof DataFindRequest) {
            return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
        } else if (request instanceof DataInsertRequest) {
            return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
        } else if (request instanceof DataSaveRequest) {
            return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
        } else if (request instanceof DataUpdateRequest) {
            return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
        }
        return httpRequest;
    }

}

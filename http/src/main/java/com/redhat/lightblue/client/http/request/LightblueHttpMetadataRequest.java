package com.redhat.lightblue.client.http.request;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;

public class LightblueHttpMetadataRequest extends AbstractLightblueHttpRequest implements LightblueHttpRequest {

    LightblueRequest request;

    public LightblueHttpMetadataRequest(LightblueRequest request) {
        this.request = request;
    }

    @Override
    public HttpRequestBase getRestRequest(String baseServiceURI) {
        if (request instanceof AbstractLightblueMetadataRequest) {
            AbstractLightblueMetadataRequest metadataRequest = (AbstractLightblueMetadataRequest) request;
            switch (metadataRequest.getOperation()) {
                case CLEAR_DEFAULT_VERSION:
                    return getHttpDelete(request.getRestURI(baseServiceURI));
                case CREATE_METADATA:
                    return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
                case CREATE_SCHEMA:
                    return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
                case GET_ENTITY_DEPENDENCIES:
                    return getHttpGet(request.getRestURI(baseServiceURI));
                case GET_ENTITY_METADATA:
                    return getHttpGet(request.getRestURI(baseServiceURI));
                case GET_ENTITY_NAMES:
                    return getHttpGet(request.getRestURI(baseServiceURI));
                case GET_ENTITY_ROLES:
                    return getHttpGet(request.getRestURI(baseServiceURI));
                case GET_ENTITY_VERSIONS:
                    return getHttpGet(request.getRestURI(baseServiceURI));
                case REMOVE_ENTITY:
                    return getHttpDelete(request.getRestURI(baseServiceURI));
                case SET_DEFAULT_VERSION:
                    return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
                case UPDATE_ENTITY_INFO:
                    return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
                case UPDATE_SCHEMA_STATUS:
                    return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
                default:
                    throw new UnsupportedOperationException("Unknown Operation type: " + request.getOperationPathParam());
            }
        }
        else {
            throw new UnsupportedOperationException("Request type is not supported: " + request.getClass());
        }
    }

}

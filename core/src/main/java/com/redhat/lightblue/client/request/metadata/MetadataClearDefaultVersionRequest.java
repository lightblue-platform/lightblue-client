package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataClearDefaultVersionRequest extends AbstractLightblueMetadataRequest {

    public MetadataClearDefaultVersionRequest() {
        super();
    }

    public MetadataClearDefaultVersionRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return "default";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.DELETE;
    }
}

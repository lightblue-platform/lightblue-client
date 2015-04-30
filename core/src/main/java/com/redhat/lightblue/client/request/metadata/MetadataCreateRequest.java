package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataCreateRequest extends AbstractLightblueMetadataRequest {

    public MetadataCreateRequest() {
        super();
    }

    public MetadataCreateRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return PATH_PARAM_CREATE_METADATA;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.PUT;
    }
}

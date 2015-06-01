package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataGetEntityMetadataRequest extends AbstractLightblueMetadataRequest {

    public MetadataGetEntityMetadataRequest() {
        super();
    }

    public MetadataGetEntityMetadataRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return "";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

}

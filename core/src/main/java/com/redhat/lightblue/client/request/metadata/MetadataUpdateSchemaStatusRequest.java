package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.enums.MetadataStatus;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataUpdateSchemaStatusRequest extends AbstractLightblueMetadataRequest {

    private MetadataStatus status;

    public MetadataUpdateSchemaStatusRequest(String entityName, String entityVersion, MetadataStatus status) {
        super(entityName, entityVersion);
        this.status = status;
    }

    @Override
    public String getOperationPathParam() {
        return status.getStatus();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.PUT;
    }

    public void setStatus(MetadataStatus status) {
        this.status = status;
    }

}

package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataUpdateSchemaStatusRequest extends AbstractLightblueMetadataRequest {

    public MetadataUpdateSchemaStatusRequest() {

    }

    public MetadataUpdateSchemaStatusRequest(String entityName, String entityVersion) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return PATH_PARAM_UPDATE_SCHEMA_STATUS;
    }

}

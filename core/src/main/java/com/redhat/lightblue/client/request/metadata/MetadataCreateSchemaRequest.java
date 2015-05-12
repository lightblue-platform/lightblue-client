package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataCreateSchemaRequest extends AbstractLightblueMetadataRequest {

    public MetadataCreateSchemaRequest() {
        super();
    }

    public MetadataCreateSchemaRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return PATH_PARAM_CREATE_SCHEMA;
    }

}

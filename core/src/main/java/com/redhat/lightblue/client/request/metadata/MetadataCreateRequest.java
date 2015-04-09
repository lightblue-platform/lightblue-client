package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataCreateRequest extends AbstractLightblueMetadataRequest {

    public MetadataCreateRequest() {

    }

    public MetadataCreateRequest(String entityName, String entityVersion) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    @Override
    public MetadataOperation getOperation() {
        return MetadataOperation.CREATE_METADATA;
    }

}

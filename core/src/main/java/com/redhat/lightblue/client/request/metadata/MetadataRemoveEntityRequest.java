package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataRemoveEntityRequest extends AbstractLightblueMetadataRequest {

    public MetadataRemoveEntityRequest() {

    }

    public MetadataRemoveEntityRequest(String entityName, String entityVersion) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    @Override
    public MetadataOperation getOperation() {
        return MetadataOperation.REMOVE_ENTITY;
    }

}

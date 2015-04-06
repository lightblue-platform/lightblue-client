package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataClearDefaultVersionRequest extends AbstractLightblueMetadataRequest {

    public MetadataClearDefaultVersionRequest() {

    }

    public MetadataClearDefaultVersionRequest(String entityName, String entityVersion) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    @Override
    public MetadataOperation getOperation() {
        return MetadataOperation.CLEAR_DEFAULT_VERSION;
    }

}

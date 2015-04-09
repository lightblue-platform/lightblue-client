package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataUpdateEntityInfoRequest extends AbstractLightblueMetadataRequest {

    public MetadataUpdateEntityInfoRequest() {

    }

    public MetadataUpdateEntityInfoRequest(String entityName, String entityVersion) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    @Override
    public MetadataOperation getOperation() {
        return MetadataOperation.UPDATE_ENTITY_INFO;
    }

}

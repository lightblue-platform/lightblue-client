package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataGetEntityMetadataRequest extends AbstractLightblueMetadataRequest {

    public MetadataGetEntityMetadataRequest() {

    }

    public MetadataGetEntityMetadataRequest(String entityName, String entityVersion) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return PATH_PARAM_GET_ENTITY_METADATA;
    }

}

package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataGetEntityVersionsRequest extends AbstractLightblueMetadataRequest {

    public MetadataGetEntityVersionsRequest() {
        super();
    }

    public MetadataGetEntityVersionsRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return PATH_PARAM_GET_ENTITY_VERSIONS;
    }

}

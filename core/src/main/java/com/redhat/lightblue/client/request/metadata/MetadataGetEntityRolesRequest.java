package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataGetEntityRolesRequest extends AbstractLightblueMetadataRequest {

    public MetadataGetEntityRolesRequest() {
        super();
    }

    public MetadataGetEntityRolesRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return PATH_PARAM_GET_ENTITY_ROLES;
    }

}

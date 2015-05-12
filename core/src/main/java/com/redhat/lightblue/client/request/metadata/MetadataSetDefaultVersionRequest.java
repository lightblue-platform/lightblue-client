package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataSetDefaultVersionRequest extends AbstractLightblueMetadataRequest {

    public MetadataSetDefaultVersionRequest() {
        super();
    }

    public MetadataSetDefaultVersionRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return PATH_PARAM_SET_DEFAULT_VERSION;
    }

}

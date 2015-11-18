package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

/**
 * POST /metadata/{entityName}/{version}/default
 */
public class MetadataSetDefaultVersionRequest extends AbstractLightblueMetadataRequest {

    public MetadataSetDefaultVersionRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return "default";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public JsonNode getBodyJson() {
        return null;
    }

}

package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

/**
 * DELETE /metadata/{entityName}/default
 */
public class MetadataClearDefaultVersionRequest extends AbstractLightblueMetadataRequest {

    public MetadataClearDefaultVersionRequest(String entityName) {
        super(entityName);
    }

    @Override
    public String getOperationPathParam() {
        return "default";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.DELETE;
    }

    @Override
    public JsonNode getBodyJson() {
        return null;
    }

}

package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

/**
 * GET /metadata/{entityName}
 */
public class MetadataGetEntityVersionsRequest extends AbstractLightblueMetadataRequest {

    public MetadataGetEntityVersionsRequest(String entityName) {
        super(entityName);
    }

    @Override
    public String getOperationPathParam() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public JsonNode getBodyJson() {
        return null;
    }

}

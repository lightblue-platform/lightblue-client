package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

/**
 * GET /metadata/{entityName}/{version}/roles
 * GET /metadata/{entityName}/roles
 * GET /metadata/roles
 */
public class MetadataGetEntityRolesRequest extends AbstractLightblueMetadataRequest {

    public MetadataGetEntityRolesRequest() {
        super(null);
    }

    public MetadataGetEntityRolesRequest(String entityName) {
        super(entityName);
    }

    public MetadataGetEntityRolesRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return "roles";
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

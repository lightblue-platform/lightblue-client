package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

/**
 * GET /metadata/{entityName}/{version}/roles GET /metadata/{entityName}/roles
 * GET /metadata/roles
 */
public class MetadataGetEntityRolesRequest extends AbstractLightblueMetadataRequest {

    public MetadataGetEntityRolesRequest() {
        super(HttpMethod.GET,"roles",null,null);
    }

    public MetadataGetEntityRolesRequest(String entityName) {
        super(HttpMethod.GET,"roles",entityName,null);
    }

    public MetadataGetEntityRolesRequest(String entityName, String entityVersion) {
        super(HttpMethod.GET,"roles",entityName, entityVersion);
    }

}

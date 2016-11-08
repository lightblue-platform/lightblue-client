package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;

/**
 * GET /metadata/{entityName}/{version}/dependencies
 */
public class MetadataGetEntityDependenciesRequest extends LightblueMetadataRequest {

    public MetadataGetEntityDependenciesRequest(String entityName, String entityVersion) {
        super(HttpMethod.GET,"dependencies",entityName, entityVersion);
    }

}

package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;

/**
 * GET /metadata/{entityName}
 */
public class MetadataGetEntityVersionsRequest extends LightblueMetadataRequest {

    public MetadataGetEntityVersionsRequest(String entityName) {
        super(HttpMethod.GET,null,entityName,null);
    }
}

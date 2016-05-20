package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;

/**
 * GET /metadata/{entityName}/{version}
 */
public class MetadataGetEntityMetadataRequest extends LightblueMetadataRequest {

    public MetadataGetEntityMetadataRequest(String entityName, String entityVersion) {
        super(HttpMethod.GET,null,entityName, entityVersion);
    }
}

package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;

/**
 * POST /metadata/{entityName}/{version}/default
 */
public class MetadataSetDefaultVersionRequest extends LightblueMetadataRequest {

    public MetadataSetDefaultVersionRequest(String entityName, String entityVersion) {
        super(HttpMethod.POST,"default",entityName, entityVersion);
    }
}

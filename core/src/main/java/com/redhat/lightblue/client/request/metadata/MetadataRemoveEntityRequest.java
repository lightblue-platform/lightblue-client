package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;

/**
 * DELETE /metadata/{entityName}
 */
public class MetadataRemoveEntityRequest extends LightblueMetadataRequest {

    public MetadataRemoveEntityRequest(String entityName) {
        super(HttpMethod.DELETE,null,entityName,null);
    }

}

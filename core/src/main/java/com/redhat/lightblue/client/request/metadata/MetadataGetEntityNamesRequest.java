package com.redhat.lightblue.client.request.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

/**
 * GET /metadata
 */
public class MetadataGetEntityNamesRequest extends AbstractLightblueMetadataRequest {

    public MetadataGetEntityNamesRequest() {
        super(HttpMethod.GET,null,null,null);
    }

}

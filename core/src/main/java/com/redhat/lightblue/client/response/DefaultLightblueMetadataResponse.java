package com.redhat.lightblue.client.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.util.JSON;

public class DefaultLightblueMetadataResponse extends AbstractLightblueResponse implements LightblueMetadataResponse {

    public DefaultLightblueMetadataResponse(String responseText) throws LightblueParseException, LightblueResponseException {
        this(responseText, JSON.getDefaultObjectMapper());
    }

    public DefaultLightblueMetadataResponse(String responseText, ObjectMapper mapper) throws LightblueParseException, LightblueResponseException {
        super(responseText, mapper);
    }

}

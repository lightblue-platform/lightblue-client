package com.redhat.lightblue.client.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.util.JSON;

public class DefaultLightblueMetadataResponse extends AbstractLightblueResponse implements LightblueMetadataResponse {

    private static final long serialVersionUID = -3347921128635969447L;

    public DefaultLightblueMetadataResponse(String responseText, Map<String, List<String>> headers) throws LightblueParseException, LightblueResponseException, LightblueException {
        this(responseText, headers, JSON.getDefaultObjectMapper());
    }

    public DefaultLightblueMetadataResponse(String responseText, Map<String, List<String>> headers, ObjectMapper mapper) throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, headers, mapper);
    }

}

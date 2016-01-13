package com.redhat.lightblue.client.response.lock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.response.DefaultLightblueErrorResponse;
import com.redhat.lightblue.client.response.LightblueParseException;
import com.redhat.lightblue.client.response.LightblueResponseException;

public class LockResponse extends DefaultLightblueErrorResponse {

    public LockResponse(String responseText)
            throws LightblueParseException, LightblueResponseException {
        super(responseText);
    }

    public LockResponse(String responseText, ObjectMapper mapper)
            throws LightblueParseException, LightblueResponseException {
        super(responseText, mapper);
    }

    public boolean parseAsBoolean() throws LightblueParseException {
        return parseResultNode().asBoolean();
    }

    public int parseAsInt() throws LightblueParseException {
        return parseResultNode().asInt();
    }

    private JsonNode parseResultNode() throws LightblueParseException {
        if (!(getJson() instanceof ObjectNode)) {
            throw new LightblueParseException("Unable to parse json: " + getJson());
        }

        return ((ObjectNode) getJson()).get("result");
    }

}

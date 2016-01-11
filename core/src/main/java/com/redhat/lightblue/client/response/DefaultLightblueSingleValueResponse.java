package com.redhat.lightblue.client.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>A special response type for responses from lightblue where only a single primitive value is expected.</p>
 * <p><b>WARNING:</b> There should be only a few special cases where this response type is needed. It is not
 * recommended for general usage.</p>
 *
 * @author dcrissman
 */
public class DefaultLightblueSingleValueResponse extends DefaultLightblueErrorResponse {

    public DefaultLightblueSingleValueResponse(String responseText)
            throws LightblueParseException, LightblueResponseException {
        super(responseText);
    }

    public DefaultLightblueSingleValueResponse(String responseText, ObjectMapper mapper)
            throws LightblueParseException, LightblueResponseException {
        super(responseText, mapper);
    }

    public DefaultLightblueSingleValueResponse(JsonNode responseNode) 
            throws LightblueResponseException {
        super(responseNode);
    }

    public DefaultLightblueSingleValueResponse(JsonNode responseNode, ObjectMapper mapper) 
            throws LightblueResponseException {
        super(responseNode, mapper);
    }

    public boolean parseAsBoolean() throws LightblueParseException {
        return parseResultNode().asBoolean();
    }

    public int parseAsInt() throws LightblueParseException {
        return parseResultNode().asInt();
    }

    public String parseAsString() throws LightblueParseException {
        return parseResultNode().asText();
    }

    public long parseAsLong() throws LightblueParseException {
        return parseResultNode().asLong();
    }

    public double parseAsDouble() throws LightblueParseException {
        return parseResultNode().asDouble();
    }

    private JsonNode parseResultNode() throws LightblueParseException {
        if (!(getJson() instanceof ObjectNode)) {
            throw new LightblueParseException("Unable to parse json: " + getJson());
        }

        return ((ObjectNode) getJson()).get("result");
    }

}

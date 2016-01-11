package com.redhat.lightblue.client.response;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.util.JSON;

public abstract class AbstractLightblueResponse implements LightblueResponse {

    private final String text;
    private JsonNode json;
    private final ObjectMapper mapper;

    public AbstractLightblueResponse(String responseText) throws LightblueParseException {
        this(responseText, JSON.getDefaultObjectMapper());
    }

    public AbstractLightblueResponse(String responseText, ObjectMapper mapper) throws LightblueParseException {
        if (mapper == null) {
            throw new NullPointerException("ObjectMapper instance cannot be null");
        }
        this.mapper = mapper;

        text = responseText;
        try {
            json = mapper.readTree(responseText);
        } catch (IOException e) {
            throw new LightblueParseException("Unable to parse response: ", e);
        }
    }

    public AbstractLightblueResponse(JsonNode responseNode) {
        this(responseNode, JSON.getDefaultObjectMapper());
    }

    public AbstractLightblueResponse(JsonNode responseNode, ObjectMapper mapper) {
        json = responseNode;
        text = responseNode.toString();
        this.mapper = mapper;
    }

    protected ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public JsonNode getJson() {
        return json;
    }

    protected int parseInt(String fieldName) {
        JsonNode field = getJson().findValue(fieldName);
        if (field == null || field.isNull()) {
            return 0;
        }
        return field.asInt();
    }

}

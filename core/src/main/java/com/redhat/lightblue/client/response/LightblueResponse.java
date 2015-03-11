package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.lang.reflect.Array;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class LightblueResponse {

    private String text;
    private JsonNode json;

    public LightblueResponse() {

    }

    public LightblueResponse(String responseText) {
        this.text = responseText;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.readTree(responseText);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse response: " + responseText, e);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JsonNode getJson() {
        return json;
    }

    public void setJson(JsonNode json) {
        this.json = json;
    }

    public boolean hasError() {
        JsonNode objectTypeNode = json.get("status");
        if (objectTypeNode == null) {
            return false;
        }

        return objectTypeNode.textValue().equalsIgnoreCase("error");
    }

    public int parseModifiedCount() {
        return parseInt("modifiedCount");
    }

    public int parseMatchCount() {
        return parseInt("matchCount");
    }

    private int parseInt(String fieldName) {
        JsonNode field = json.findValue(fieldName);
        if (field == null || field.isNull()) {
            return 0;
        }
        return field.asInt();
    }

    public <T> T parseProcessed(final Class<T> type) throws LightblueResponseParseException {
        return parseProcessed(new ObjectMapper(), type);
    }

    @SuppressWarnings("unchecked")
    public <T> T parseProcessed(final ObjectMapper mapper, final Class<T> type)
            throws LightblueResponseParseException {
        JsonNode processedNode = json.path("processed");
        try {
            //if null or an empty array
            if (processedNode == null
                    || processedNode.isNull()
                    || (processedNode.isArray() && !((ArrayNode) processedNode).iterator().hasNext())) {
                if (type.isArray()) {
                    return (T) Array.newInstance(type.getComponentType(), 0);
                }
                return null;
            }

            return mapper.readValue(processedNode.traverse(), type);
        } catch (RuntimeException | IOException e) {
            throw new LightblueResponseParseException("Error parsing lightblue response: " + json.toString() + "\n", e);
        }
    }

}

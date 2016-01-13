package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.lang.reflect.Array;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.LightblueException;

public class DefaultLightblueDataResponse extends AbstractLightblueResponse implements LightblueDataResponse {

    public DefaultLightblueDataResponse(JsonNode responseNode)
            throws LightblueResponseException, LightblueException {
        super(responseNode);
    }

    public DefaultLightblueDataResponse(JsonNode responseNode, ObjectMapper mapper)
            throws LightblueResponseException, LightblueException {
        super(responseNode, mapper);
    }

    public DefaultLightblueDataResponse(String responseText)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText);
    }

    public DefaultLightblueDataResponse(String responseText, ObjectMapper mapper)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, mapper);
    }

    @Override
    public int parseModifiedCount() {
        return parseInt("modifiedCount");
    }

    @Override
    public int parseMatchCount() {
        return parseInt("matchCount");
    }

    @Override
    public JsonNode getProcessed() {
        return getJson().get("processed");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T parseProcessed(final Class<T> type) throws LightblueParseException {
        if (hasAnyErrors()) {
            throw new LightblueParseException("Error returned in response: " + getText());
        }

        try {
            JsonNode processedNode = getProcessed();

            // if null or an empty array
            if (processedNode == null || processedNode.isNull() || processedNode.isMissingNode() || (processedNode.isArray() && !((ArrayNode) processedNode).iterator()
                    .hasNext())) {
                if (type.isArray()) {
                    return (T) Array.newInstance(type.getComponentType(), 0);
                }
                return null;
            }
            if (!type.isArray()) {
                if (processedNode.size() > 1) {
                    throw new LightblueParseException("Was expecting single result:" + getText() + "\n");
                } else {
                    return getMapper().readValue(processedNode.get(0).traverse(), type);
                }
            } else {
                return getMapper().readValue(processedNode.traverse(), type);
            }
        } catch (RuntimeException | IOException e) {
            throw new LightblueParseException("Error parsing lightblue response: " + getText() + "\n", e);
        }
    }

    protected int parseInt(String fieldName) {
        JsonNode field = getJson().findValue(fieldName);
        if (field == null || field.isNull()) {
            return 0;
        }
        return field.asInt();
    }

}

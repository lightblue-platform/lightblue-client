package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.LightblueException;

public class DefaultLightblueDataResponse extends AbstractLightblueResponse implements LightblueDataResponse {

    private static final long serialVersionUID = 9125163307190941424L;

    public DefaultLightblueDataResponse(JsonNode responseNode, Map<String, List<String>> headers)
            throws LightblueResponseException, LightblueException {
        super(responseNode, headers);
    }

    public DefaultLightblueDataResponse(JsonNode responseNode, ObjectMapper mapper) throws LightblueResponseException, LightblueException {
        super(responseNode, null, mapper);
    }

    public DefaultLightblueDataResponse(JsonNode responseNode, Map<String, List<String>> headers, ObjectMapper mapper)
            throws LightblueResponseException, LightblueException {
        super(responseNode, headers, mapper);
    }

    public DefaultLightblueDataResponse(String responseText) throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, null);
    }

    public DefaultLightblueDataResponse(String responseText, Map<String, List<String>> headers)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, headers);
    }

    public DefaultLightblueDataResponse(String responseText, Map<String, List<String>> headers, ObjectMapper mapper)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, headers, mapper);
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
                    JsonNode resultNode = processedNode.get(0);
                    if (resultNode != null) {
                        return getMapper().readValue(resultNode.traverse(), type);
                    }
                    return null;
                }
            } else {
                return getMapper().readValue(processedNode.traverse(), type);
            }
        } catch (RuntimeException | IOException e) {
            throw new LightblueParseException("Error parsing lightblue response: " + getText() + "\n", e);
        }
    }

    protected int parseInt(String fieldName) {
        JsonNode field = getJson().get(fieldName);
        if (field == null || field.isNull()) {
            return 0;
        }
        return field.asInt();
    }

}

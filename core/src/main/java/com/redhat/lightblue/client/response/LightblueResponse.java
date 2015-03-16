package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.lang.reflect.Array;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.util.ClientConstants;

public class LightblueResponse {

    /**
     * It is safe and encouraged to share the same mapper among threads. It is
     * thread safe. So, this default instance is static.
     *
     * @see <a href="http://stackoverflow.com/a/3909846">The developer of the
     * Jackson library's own quote.</a>
     */
    public static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper()
            .setDateFormat(ClientConstants.getDateFormat())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String text;
    private JsonNode json;
    private final ObjectMapper mapper;

    public LightblueResponse(ObjectMapper mapper) {
        if (mapper == null) {
            throw new NullPointerException("ObjectMapper instance cannot be null");
        }
        this.mapper = mapper;
    }

    public LightblueResponse() {
        this(DEFAULT_MAPPER);
    }

    public LightblueResponse(String responseText) {
        this(responseText, DEFAULT_MAPPER);
    }

    public LightblueResponse(String responseText, ObjectMapper mapper) {
        this(mapper);
        this.text = responseText;
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

    @SuppressWarnings("unchecked")
    public <T> T parseProcessed(final Class<T> type)
            throws LightblueResponseParseException {
        if (hasError()) {
            throw new LightblueErrorResponseException("Error returned in response: " + getText());
        }

        try {
            JsonNode processedNode = json.path("processed");

            //if null or an empty array
            if (processedNode == null
                    || processedNode.isNull()
                    || processedNode.isMissingNode()
                    || (processedNode.isArray() && !((ArrayNode) processedNode).iterator().hasNext())) {
                if (type.isArray()) {
                    return (T) Array.newInstance(type.getComponentType(), 0);
                }
                return null;
            }

            return mapper.readValue(processedNode.traverse(), type);
        } catch (RuntimeException | IOException e) {
            throw new LightblueResponseParseException("Error parsing lightblue response: " + getText() + "\n", e);
        }
    }

}

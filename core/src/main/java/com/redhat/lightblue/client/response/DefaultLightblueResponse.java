package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;
import com.redhat.lightblue.client.util.JSON;

public class DefaultLightblueResponse implements LightblueResponse, LightblueErrorResponse {

    private String text;
    private JsonNode json;
    private final ObjectMapper mapper;

    public DefaultLightblueResponse(ObjectMapper mapper) {
        if (mapper == null) {
            throw new NullPointerException("ObjectMapper instance cannot be null");
        }
        this.mapper = mapper;
    }

    public DefaultLightblueResponse() {
        this(JSON.getDefaultObjectMapper());
    }

    public DefaultLightblueResponse(String responseText) throws LightblueException {
        this(responseText, JSON.getDefaultObjectMapper());
    }

    public DefaultLightblueResponse(String responseText, ObjectMapper mapper) throws LightblueException {
        this(mapper);
        text = responseText;
        try {
            json = mapper.readTree(responseText);

            if (hasAnyErrors()) {
                throw new LightblueResponseException("Lightblue exception occurred: ", this);
            }
        } catch (IOException e) {
            throw new LightblueParseException("Unable to parse response: ", e);
        }
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public JsonNode getJson() {
        return json;
    }

    public void setJson(JsonNode json) {
        this.json = json;
    }

    @Override
    public boolean hasDataErrors() {
        if (json == null) {
            return false;
        }

        JsonNode err = json.get("dataErrors");
        return err != null && !(err instanceof NullNode) && err.size() > 0;
    }

    @Override
    public boolean hasLightblueErrors() {
        if (json == null) {
            return true;
        }

        JsonNode objectTypeNode = json.get("status");
        if(objectTypeNode != null
                && (objectTypeNode.textValue().equalsIgnoreCase("error")
                  || objectTypeNode.textValue().equalsIgnoreCase("partial"))){
            return true;
        }

        JsonNode err = json.get("errors");
        return err != null && !(err instanceof NullNode) && err.size() > 0;
    }

    public boolean hasAnyErrors() {
        return hasDataErrors() || hasLightblueErrors();
    }

    @Override
    public DataError[] getDataErrors() {
        List<DataError> list = new ArrayList<>();
        if (json == null) {
            return null;
        }
        JsonNode err = json.get("dataErrors");
        if (err instanceof ObjectNode) {
            list.add(DataError.fromJson((ObjectNode) err));
        } else if (err instanceof ArrayNode) {
            for (Iterator<JsonNode> itr = ((ArrayNode) err).elements(); itr.hasNext();) {
                list.add(DataError.fromJson((ObjectNode) itr.next()));
            }
        } else {
            return null;
        }
        return list.toArray(new DataError[list.size()]);
    }

    @Override
    public Error[] getLightblueErrors() {
        List<Error> list = new ArrayList<>();
        if (json == null) {
            return null;
        }
        JsonNode err = json.get("errors");
        if (err instanceof ObjectNode) {
            list.add(Error.fromJson(err));
        } else if (err instanceof ArrayNode) {
            for (Iterator<JsonNode> itr = ((ArrayNode) err).elements(); itr.hasNext();) {
                list.add(Error.fromJson(itr.next()));
            }
        } else {
            return null;
        }
        return list.toArray(new Error[list.size()]);
    }

    @Override
    public int parseModifiedCount() {
        return parseInt("modifiedCount");
    }

    @Override
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

    @Override
    public JsonNode getProcessed() {
        return json.get("processed");
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
                    return mapper.readValue(processedNode.get(0).traverse(), type);
                }
            } else {
                return mapper.readValue(processedNode.traverse(), type);
            }
        } catch (RuntimeException | IOException e) {
            throw new LightblueParseException("Error parsing lightblue response: " + getText() + "\n", e);
        }
    }

}

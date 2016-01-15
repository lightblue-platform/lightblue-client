package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;
import com.redhat.lightblue.client.util.JSON;

public abstract class AbstractLightblueResponse implements LightblueResponse, LightblueErrorResponse {

    private final String text;
    private JsonNode json;
    private final ObjectMapper mapper;

    public AbstractLightblueResponse(String responseText) throws LightblueParseException, LightblueResponseException, LightblueException {
        this(responseText, JSON.getDefaultObjectMapper());
    }

    public AbstractLightblueResponse(String responseText, ObjectMapper mapper) throws LightblueParseException, LightblueResponseException, LightblueException {
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

        assertNoErrors();
    }

    public AbstractLightblueResponse(JsonNode responseNode) throws LightblueResponseException, LightblueException {
        this(responseNode, JSON.getDefaultObjectMapper());
    }

    public AbstractLightblueResponse(JsonNode responseNode, ObjectMapper mapper) throws LightblueResponseException, LightblueException {
        json = responseNode;
        text = responseNode.toString();
        this.mapper = mapper;

        assertNoErrors();
    }

    protected void assertNoErrors() throws LightblueResponseException, LightblueException {
        if ((getJson() == null) || hasAnyErrors()) {
            throw new LightblueResponseException("Error returned from lightblue.", this);
        }
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

    public boolean hasAnyErrors() {
        return hasDataErrors() || hasLightblueErrors();
    }

    @Override
    public boolean hasDataErrors() {
        if (getJson() == null) {
            return false;
        }

        JsonNode err = getJson().get("dataErrors");
        return err != null && !(err instanceof NullNode) && err.size() > 0;
    }

    @Override
    public boolean hasLightblueErrors() {
        if (getJson() == null) {
            return false;
        }

        JsonNode objectTypeNode = getJson().get("status");
        if (objectTypeNode != null && (objectTypeNode.textValue().equalsIgnoreCase(
                "error") || objectTypeNode.textValue().equalsIgnoreCase(
                        "partial"))) {
            return true;
        }

        JsonNode err = getJson().get("errors");
        return err != null && !(err instanceof NullNode) && err.size() > 0;
    }

    @Override
    public DataError[] getDataErrors() {
        List<DataError> list = new ArrayList<>();
        if (getJson() == null) {
            return null;
        }
        JsonNode err = getJson().get("dataErrors");
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
        if (getJson() == null) {
            return null;
        }
        JsonNode err = getJson().get("errors");
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

}

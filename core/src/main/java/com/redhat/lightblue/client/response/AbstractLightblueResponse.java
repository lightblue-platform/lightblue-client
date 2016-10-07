package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;
import com.redhat.lightblue.client.util.JSON;

public abstract class AbstractLightblueResponse implements LightblueResponse, LightblueErrorResponse, Serializable {

    private static final long serialVersionUID=1l;

    public static final String HEADER_REQUEST_ID = "RequestID";

    private final String text;
    private JsonNode json;
    private final ObjectMapper mapper;
    private DataError[] dataErrorCache;
    private Error[] errorCache;
    private final Map<String, List<String>> headers;

    public AbstractLightblueResponse(String responseText, Map<String, List<String>> headers) throws LightblueParseException, LightblueResponseException, LightblueException {
        this(responseText, headers, JSON.getDefaultObjectMapper());
    }

    public AbstractLightblueResponse(String responseText, Map<String, List<String>> headers, ObjectMapper mapper) throws LightblueParseException, LightblueResponseException, LightblueException {
        if (mapper == null) {
            throw new NullPointerException("ObjectMapper instance cannot be null");
        }
        this.mapper = mapper;
        this.headers = headers;

        text = responseText;
        try {
            json = mapper.readTree(responseText);
        } catch (IOException e) {
            throw new LightblueParseException("Unable to parse response: ", e);
        }

        assertNoErrors();
    }

    public AbstractLightblueResponse(JsonNode responseNode, Map<String, List<String>> headers) throws LightblueResponseException, LightblueException {
        this(responseNode, headers, JSON.getDefaultObjectMapper());
    }

    public AbstractLightblueResponse(JsonNode responseNode, Map<String, List<String>> headers, ObjectMapper mapper) throws LightblueResponseException, LightblueException {
        json = responseNode;
        text = responseNode.toString();
        this.mapper = mapper;
        this.headers = headers;

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
        if (objectTypeNode != null && (objectTypeNode.textValue().equalsIgnoreCase("error")
                || objectTypeNode.textValue().equalsIgnoreCase("partial"))) {
            return true;
        }

        JsonNode err = getJson().get("errors");
        return err != null && !(err instanceof NullNode) && err.size() > 0;
    }

    @Override
    public DataError[] getDataErrors() {
        if (dataErrorCache == null) {
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
            dataErrorCache = list.toArray(new DataError[list.size()]);
        }
        return dataErrorCache;
    }

    @Override
    public Error[] getLightblueErrors() {
        if (errorCache == null) {
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
            errorCache = list.toArray(new Error[list.size()]);
        }
        return errorCache;
    }

    @Override
    public List<String> getHeaderValues(String key) {
        if (headers == null) {
            return null;
        }
        return headers.get(key);
    }

    @Override
    public String getRequestId() {
        List<String> values = getHeaderValues(HEADER_REQUEST_ID);
        if (values == null || values.isEmpty()) {
            return null;
        }
        //There should only ever be a single value for this header.
        return values.get(0);
    }

}

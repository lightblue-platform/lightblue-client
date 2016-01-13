package com.redhat.lightblue.client.response;

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

/**
 * Represents a response that may contain an error or data error from lightblue.
 *
 * @author dcrissman
 */
public class DefaultLightblueErrorResponse extends AbstractLightblueResponse implements LightblueErrorResponse {

    public DefaultLightblueErrorResponse(JsonNode responseNode) 
            throws LightblueResponseException, LightblueException {
        this(responseNode, JSON.getDefaultObjectMapper());
    }

    public DefaultLightblueErrorResponse(JsonNode responseNode, ObjectMapper mapper) 
            throws LightblueResponseException, LightblueException {
        super(responseNode, mapper);
        assertNoErrors();
    }

    public DefaultLightblueErrorResponse(String responseText) 
            throws LightblueParseException, LightblueResponseException, LightblueException {
        this(responseText, JSON.getDefaultObjectMapper());
    }

    public DefaultLightblueErrorResponse(String responseText, ObjectMapper mapper) 
            throws LightblueResponseException, LightblueParseException, LightblueException {
        super(responseText, mapper);
        assertNoErrors();
    }

    protected void assertNoErrors() throws LightblueResponseException, LightblueException {
        if ((getJson() == null) || hasAnyErrors()) {
            throw new LightblueResponseException("Error returned from lightblue.", this);
        }
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

    public boolean hasAnyErrors() {
        return hasDataErrors() || hasLightblueErrors();
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

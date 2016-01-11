package com.redhat.lightblue.client.response;

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

/**
 * <p>A special response type for responses from lightblue where only a single primitive value is expected.</p>
 * <p><b>WARNING:</b> There should be only a few special cases where this response type is needed. It is not
 * recommended for general usage.</p>
 *
 * @author dcrissman
 */
public class DefaultLightblueSingleValueResponse extends AbstractLightblueResponse implements LightblueResponse, LightblueErrorResponse {

    public DefaultLightblueSingleValueResponse(String responseText) throws LightblueParseException, LightblueResponseException {
        this(responseText, JSON.getDefaultObjectMapper());
    }

    public DefaultLightblueSingleValueResponse(String responseText, ObjectMapper mapper) throws LightblueParseException, LightblueResponseException {
        super(responseText, mapper);

        if (hasAnyErrors()) {
            throw new LightblueResponseException("Error returned from lightblue.", this);
        }
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
                "error") || objectTypeNode.textValue().equalsIgnoreCase("partial"))) {
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

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

public class DefaultLightblueDataResponse extends AbstractLightblueResponse implements LightblueDataResponse, LightblueErrorResponse {

    public DefaultLightblueDataResponse(String responseText) throws LightblueParseException, LightblueResponseException {
        this(responseText, JSON.getDefaultObjectMapper());
    }

    public DefaultLightblueDataResponse(String responseText, ObjectMapper mapper) throws LightblueParseException, LightblueResponseException {
        super(responseText, mapper);

        if (hasAnyErrors()) {
            throw new LightblueResponseException("Lightblue exception occurred: ", this);
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
            return true;
        }

        JsonNode objectTypeNode = getJson().get("status");
        if(objectTypeNode != null
                && (objectTypeNode.textValue().equalsIgnoreCase("error")
                  || objectTypeNode.textValue().equalsIgnoreCase("partial"))){
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

}

package com.redhat.lightblue.client.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.io.Serializable;

/**
 * Base class for objects constructed from Json nodes. Optionally contains a
 * pointer to the json node the object is constructed from.
 */
public abstract class JsonObject implements Serializable {

    private static final long serialVersionUID = 1l;

    private static JsonNodeFactory factory = JsonNodeFactory.withExactBigDecimals(true);

    /**
     * @return the factory
     */
    public static JsonNodeFactory getFactory() {
        return factory;
    }

    private JsonNode sourceNode;

    public JsonObject() {
    }

    public JsonObject(JsonNode node) {
        sourceNode = node;
    }

    public JsonNode getSourceNode() {
        return sourceNode;
    }

    public abstract JsonNode toJson();

    @Override
    public String toString() {
        return toJson().toString();
    }
}

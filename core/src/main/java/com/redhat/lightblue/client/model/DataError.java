package com.redhat.lightblue.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents errors related to data in a document
 */
public class DataError {

    private JsonNode entityData;
    private List<Error> errors;

    /**
     * Default ctor
     */
    public DataError() {
    }

    /**
     * Ctor with given values
     */
    public DataError(JsonNode entityData, List<Error> errors) {
        this.entityData = entityData;
        this.errors = errors;
    }

    /**
     * The entity data for which these errors apply. Generated using the same
     * projection as the API generated this error.
     */
    public JsonNode getEntityData() {
        return entityData;
    }

    /**
     * The entity data for which these errors apply. Generated using the same
     * projection as the API generated this error.
     */
    public void setEntityData(JsonNode node) {
        entityData = node;
    }

    /**
     * List of errors for this document
     */
    public List<Error> getErrors() {
        return errors;
    }

    /**
     * List of errors for this document
     */
    public void setErrors(List<Error> e) {
        errors = e;
    }

    /**
     * Parses a Json object node and returns the DataError corresponding to it.
     * It is up to the client to make sure that the object node is a DataError
     * representation. Any unrecognized elements are ignored.
     */
    public static DataError fromJson(ObjectNode node) {
        DataError error = new DataError();
        JsonNode x = node.get("data");
        if (x != null) {
            error.entityData = x;
        }
        x = node.get("errors");
        if (x instanceof ArrayNode) {
            error.errors = new ArrayList<>();
            for (Iterator<JsonNode> itr = ((ArrayNode) x).elements();
                    itr.hasNext();) {
                error.errors.add(Error.fromJson(itr.next()));
            }
        }
        return error;
    }

    /**
     * Returns the data error for the given json doc in the list
     */
    public static DataError findErrorForDoc(List<DataError> list, JsonNode node) {
        for (DataError x : list) {
            if (x.entityData == node) {
                return x;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "DataError [entityData=" + entityData + ", errors=" + errors + "]";
    }

}

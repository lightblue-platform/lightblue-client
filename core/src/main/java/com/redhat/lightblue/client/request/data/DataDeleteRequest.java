package com.redhat.lightblue.client.request.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.CRUDRequest;

public class DataDeleteRequest extends CRUDRequest {

    private Query queryExpression;

    public DataDeleteRequest(String entityName, String entityVersion) {
        super(HttpMethod.POST, "delete", entityName, entityVersion);
    }

    public DataDeleteRequest(String entityName) {
        this(entityName,null);
    }

    public DataDeleteRequest where(com.redhat.lightblue.client.Query queryExpression) {
        this.queryExpression = queryExpression;

        return this;
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node = (ObjectNode) super.getBodyJson();
        if (queryExpression != null) {
            node.set("query", queryExpression.toJson());
        }
        return node;
    }

    @Override
    public Operation getOperation() {
        return Operation.DELETE;
    }
}

package com.redhat.lightblue.client.request.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataExecutionRequest;

public class DataDeleteRequest extends AbstractLightblueDataExecutionRequest {
    private Query queryExpression;

    public DataDeleteRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public DataDeleteRequest(String entityName) {
        super(entityName);
    }

    public DataDeleteRequest where(com.redhat.lightblue.client.Query queryExpression) {
        this.queryExpression = queryExpression;

        return this;
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node = (ObjectNode)super.getBodyJson();
        if (queryExpression != null) {
            node.set("query", queryExpression.toJson());
        }
        return node;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getOperationPathParam() {
        return "delete";
    }

    @Override
    public Operation getOperation() {
        return Operation.DELETE;
    }
}

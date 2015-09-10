package com.redhat.lightblue.client.request.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

public class DataDeleteRequest extends AbstractLightblueDataRequest {
    private Query queryExpression;

    public DataDeleteRequest() {
        super();
    }

    public DataDeleteRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public DataDeleteRequest(String entityName) {
        super(entityName);
    }

    @Deprecated
    public void where(com.redhat.lightblue.client.expression.query.Query queryExpression) {
        this.queryExpression = toq(queryExpression);
    }

    public void where(com.redhat.lightblue.client.Query queryExpression) {
        this.queryExpression = queryExpression;
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node=JsonNodeFactory.instance.objectNode();
        node.set("query",queryExpression.toJson());
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

package com.redhat.lightblue.client.request.data;

import com.fasterxml.jackson.databind.JsonNode;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.util.JSON;

/**
 * An operation non-specific {@link AbstractLightblueDataRequest} for when the json for the body has already been generated through some other means. <br/>
 * Should not be preferred over the operation specific implementations.
 *
 * @author dcrissman
 */
public class LiteralDataRequest extends AbstractLightblueDataRequest {

    private final JsonNode body;
    private final HttpMethod httpMethod;
    private final String operationPathParam;
    private final String operation;

    public String getOperation() {
        return operation;
    }

    @Deprecated
    public LiteralDataRequest(String body, HttpMethod httpMethod, String operationalPathParam) {
        super();
        this.body = JSON.toJsonNode(body);
        this.httpMethod = httpMethod;
        this.operationPathParam = operationalPathParam;
        this.operation = this.operationPathParam;
    }

    @Deprecated
    public LiteralDataRequest(String entityName, String entityVersion, String body, HttpMethod httpMethod, String operationalPathParam) {
        super(entityName, entityVersion);
        this.body = JSON.toJsonNode(body);
        this.httpMethod = httpMethod;
        this.operationPathParam = operationalPathParam;
        this.operation = this.operationPathParam;
    }

    @Deprecated
    public LiteralDataRequest(JsonNode body, HttpMethod httpMethod, String operationalPathParam) {
        super();
        this.body = body;
        this.httpMethod = httpMethod;
        this.operationPathParam = operationalPathParam;
        this.operation = this.operationPathParam;
    }

    @Deprecated
    public LiteralDataRequest(String entityName, String entityVersion, JsonNode body, HttpMethod httpMethod, String operationalPathParam) {
        super(entityName, entityVersion);
        this.body = body;
        this.httpMethod = httpMethod;
        this.operationPathParam = operationalPathParam;
        this.operation = this.operationPathParam;
    }

    public LiteralDataRequest(JsonNode body, HttpMethod httpMethod, String operationPathParam, String operation) {
        super();
        this.body = body;
        this.httpMethod = httpMethod;
        this.operationPathParam = operationPathParam;
        this.operation = operation;
    }

    public LiteralDataRequest(String entityName, String entityVersion, JsonNode body, HttpMethod httpMethod, String operationalPathParam, String operation) {
        super(entityName, entityVersion);
        this.body = body;
        this.httpMethod = httpMethod;
        this.operationPathParam = operationalPathParam;
        this.operation = this.operationPathParam;
    }

    @Override
    public JsonNode getBodyJson() {
        return body;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    @Override
    public String getOperationPathParam() {
        return operationPathParam;
    }

}

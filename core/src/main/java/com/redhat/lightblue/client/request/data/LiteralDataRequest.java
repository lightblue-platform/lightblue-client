package com.redhat.lightblue.client.request.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

/**
 * An operation non-specific {@link AbstractLightblueDataRequest} for when the
 * json for the body has already been generated through some other means. <br/>
 * Should not be preferred over the operation specific implementations.
 *
 * @author dcrissman
 */
public class LiteralDataRequest extends AbstractLightblueDataRequest {

    private final JsonNode body;
    private final HttpMethod httpMethod;
    private final String operationPathParam;
    private final Operation operation;

    public LiteralDataRequest(String entityName, String entityVersion, JsonNode body, HttpMethod httpMethod, String operationalPathParam, Operation operation) {
        super(entityName, entityVersion);
        this.body = body;
        this.httpMethod = httpMethod;
        operationPathParam = operationalPathParam;
        this.operation = Operation.valueOf(operationalPathParam.toUpperCase());
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

    @Override
    public Operation getOperation() {
        return operation;
    }

}

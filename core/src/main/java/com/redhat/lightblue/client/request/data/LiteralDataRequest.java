package com.redhat.lightblue.client.request.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.CRUDRequest;

/**
 * An operation non-specific {@link AbstractLightblueDataRequest} for when the
 * json for the body has already been generated through some other means. <br/>
 * Should not be preferred over the operation specific implementations.
 *
 * @author dcrissman
 */
public class LiteralDataRequest extends CRUDRequest {

    private final JsonNode body;
    private final Operation operation;

    public LiteralDataRequest(String entityName, String entityVersion, JsonNode body, HttpMethod httpMethod, String operationPathParam, Operation operation) {
        super(httpMethod,operationPathParam,entityName, entityVersion);
        this.body = body;
        this.operation = Operation.valueOf(operationPathParam.toUpperCase());
    }

    @Override
    public JsonNode getBodyJson() {
        return body;
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

}

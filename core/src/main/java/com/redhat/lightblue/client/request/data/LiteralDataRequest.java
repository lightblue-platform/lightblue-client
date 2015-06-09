package com.redhat.lightblue.client.request.data;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

public class LiteralDataRequest extends AbstractLightblueDataRequest {

    private final String body;
    private final HttpMethod httpMethod;
    private final String operationPathParam;

    public LiteralDataRequest(String body, HttpMethod httpMethod, String operationalPathParam) {
        super();
        this.body = body;
        this.httpMethod = httpMethod;
        this.operationPathParam = operationalPathParam;
    }

    public LiteralDataRequest(String entityName, String entityVersion, String body, HttpMethod httpMethod, String operationalPathParam) {
        super(entityName, entityVersion);
        this.body = body;
        this.httpMethod = httpMethod;
        this.operationPathParam = operationalPathParam;
    }

    @Override
    public String getBody() {
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

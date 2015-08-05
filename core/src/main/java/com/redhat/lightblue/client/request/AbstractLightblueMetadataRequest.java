package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import com.redhat.lightblue.client.util.JSON;

public abstract class AbstractLightblueMetadataRequest extends AbstractLightblueRequest {
    private JsonNode body;

    public void setBody(JsonNode body) {
        this.body=body;
    }

    @Override
    public JsonNode getBodyJson() {
        return body;
    }
    
    @Override
    public String getBody() {
        return getBodyJson().toString();
    }

    @Deprecated
    public void setBody(String body) {
        this.body = body==null?null:JSON.toJsonNode(body);
    }

    public AbstractLightblueMetadataRequest() {
        super();
    }

    public AbstractLightblueMetadataRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public AbstractLightblueMetadataRequest(String entityName) {
        super(entityName);
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        StringBuilder requestURI = new StringBuilder();

        requestURI.append(baseServiceURI);

        if (StringUtils.isNotBlank(this.getEntityName())) {
            appendToURI(requestURI, this.getEntityName());
        }

        if (StringUtils.isNotBlank(this.getEntityVersion())) {
            appendToURI(requestURI, this.getEntityVersion());
        }

        if (StringUtils.isNotBlank(this.getOperationPathParam())) {
            appendToURI(requestURI, getOperationPathParam());
        }

        return requestURI.toString();
    }

    public abstract String getOperationPathParam();
}

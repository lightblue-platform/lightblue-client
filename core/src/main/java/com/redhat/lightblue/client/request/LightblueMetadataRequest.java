package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import com.redhat.lightblue.client.http.HttpMethod;

public abstract class LightblueMetadataRequest extends LightblueRequest {

    protected final String operationName;
    protected final String entityName;
    protected final String entityVersion;

    public LightblueMetadataRequest(HttpMethod method,String operationName,String entityName, String entityVersion) {
        super(method);
        this.operationName=operationName;
        this.entityName=entityName;
        this.entityVersion=entityVersion;
    }

    public LightblueMetadataRequest(HttpMethod method,String operationName,String entityName) {
        this(method,operationName,entityName,null);
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public String getOperationPathParam() {
        return operationName;
    }

    /**
     * Default implementation returns the following, omitting any null section:
     * <pre>
     *  baseServiceURI / entity / version
     * </pre>
     */
    @Override
    public String getRestURI(String baseServiceURI) {

        StringBuilder requestURI = new StringBuilder();
        
        requestURI.append(baseServiceURI);

        if (StringUtils.isNotBlank(entityName)) {
            appendToURI(requestURI, entityName);
            if (StringUtils.isNotBlank(entityVersion)) {
                appendToURI(requestURI, entityVersion);
            }
        }
        if (StringUtils.isNotBlank(getOperationPathParam())) {
            appendToURI(requestURI, getOperationPathParam());
        }

        return requestURI.toString();
    }

    @Override
    public JsonNode getBodyJson() {
        return null;
    }
}

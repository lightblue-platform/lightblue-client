package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractLightblueMetadataRequest extends AbstractLightblueRequest {

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

        if (StringUtils.isNotBlank(getEntityName())) {
            appendToURI(requestURI, getEntityName());
        }

        if (StringUtils.isNotBlank(getEntityVersion())) {
            appendToURI(requestURI, getEntityVersion());
        }

        if (StringUtils.isNotBlank(getOperationPathParam())) {
            appendToURI(requestURI, getOperationPathParam());
        }

        return requestURI.toString();
    }

    @Override
    public String getBody() {
        JsonNode body = getBodyJson();
        if (body == null) {
            return null;
        }
        return getBodyJson().toString();
    }

    public abstract String getOperationPathParam();

}

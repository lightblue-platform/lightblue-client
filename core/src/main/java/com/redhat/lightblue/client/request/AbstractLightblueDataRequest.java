package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Execution;
import com.redhat.lightblue.client.Operation;

public abstract class AbstractLightblueDataRequest extends AbstractLightblueRequest implements LightblueRequest {

    private Execution execution = null;

    public AbstractLightblueDataRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public AbstractLightblueDataRequest(String entityName) {
        super(entityName);
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        StringBuilder requestURI = new StringBuilder();

        requestURI.append(baseServiceURI);

        if (StringUtils.isNotBlank(getOperationPathParam())) {
            appendToURI(requestURI, getOperationPathParam());
        }

        if (StringUtils.isNotBlank(getEntityName())) {
            appendToURI(requestURI, getEntityName());
        }

        if (StringUtils.isNotBlank(getEntityVersion())) {
            appendToURI(requestURI, getEntityVersion());
        }
        return requestURI.toString();
    }

    protected void appendRangeToJson(ObjectNode node, Integer begin, Integer maxResults) {
        if (begin != null) {
            node.set("from", JsonNodeFactory.instance.numberNode(begin));
            if (maxResults != null) {
                node.set("maxResults", JsonNodeFactory.instance.numberNode(maxResults));
            }
        }
    }

    public void execution(Execution execution) {
        this.execution = execution;
    }

    protected void appendExecutionToJson(ObjectNode node) {
        if (execution != null)
            node.set("execution", execution.toJson());
    }

    @Override
    public String getBody() {
        return (getBodyJson() == null ? null : getBodyJson().toString());
    }

    public abstract String getOperationPathParam();

    public abstract Operation getOperation();

}

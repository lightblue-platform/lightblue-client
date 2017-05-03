package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import com.redhat.lightblue.client.Execution;

import com.redhat.lightblue.client.http.HttpMethod;

public abstract class LightblueDataRequest extends LightblueRequest {
    
    protected Execution execution = null;

    protected final String operationName;
    protected final String entityName;
    protected final String entityVersion;

    public LightblueDataRequest(HttpMethod method, String operationName, String entityName, String entityVersion) {
        super(method);
        this.operationName=operationName;
        this.entityName=entityName;
        this.entityVersion=entityVersion;
    }

    public LightblueDataRequest(LightblueDataRequest r) {
        super(r);
        this.operationName=r.operationName;
        this.entityName=r.entityName;
        this.entityVersion=r.entityVersion;
    }

    public String getOperationPathParam() {
        return operationName;
    }
    
    public String getEntityName() {
        return entityName;
    }

    public String getEntityVersion() {
        return entityVersion;
    }
    
    public LightblueDataRequest execution(Execution execution) {
        this.execution = execution;
        return this;
    }

    /**
     * Default implementation returns the following, omitting any null section:
     * <pre>
     *  baseServiceURI / operationName / entity / version
     * </pre>
     */
    @Override
    public String getRestURI(String baseServiceURI) {

        StringBuilder requestURI = new StringBuilder();
        
        requestURI.append(baseServiceURI);

        if (StringUtils.isNotBlank(operationName)) {
            appendToURI(requestURI, operationName);
        }

        if (StringUtils.isNotBlank(entityName)) {
            appendToURI(requestURI, entityName);
            if (StringUtils.isNotBlank(entityVersion)) {
                appendToURI(requestURI, entityVersion);
            }
        }
        return requestURI.toString();
    }

    public boolean hasExecution() {
        return execution != null;
    }
    
    protected void appendExecutionToJson(ObjectNode node) {
        if (execution != null) {
            node.set("execution", execution.toJson());
        }
    }

    protected static void appendRangeToJson(ObjectNode node, Integer begin, Integer maxResults) {
        if (begin != null) {
            node.set("from", JsonNodeFactory.instance.numberNode(begin));
            if (maxResults != null) {
                node.set("maxResults", JsonNodeFactory.instance.numberNode(maxResults));
            }
        }
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        appendExecutionToJson(node);
        return node;
    }
}

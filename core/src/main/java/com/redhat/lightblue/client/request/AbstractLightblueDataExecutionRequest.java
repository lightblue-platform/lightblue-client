package com.redhat.lightblue.client.request;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Execution;

public abstract class AbstractLightblueDataExecutionRequest extends AbstractLightblueDataRequest {

    public AbstractLightblueDataExecutionRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public AbstractLightblueDataExecutionRequest(String entityName) {
        super(entityName);
    }

    private Execution execution = null;

    public AbstractLightblueDataExecutionRequest execution(Execution execution) {
        this.execution = execution;
        return this;
    }

    protected void appendExecutionToJson(ObjectNode node) {
        if (execution != null)
            node.set("execution", execution.toJson());
    }

}

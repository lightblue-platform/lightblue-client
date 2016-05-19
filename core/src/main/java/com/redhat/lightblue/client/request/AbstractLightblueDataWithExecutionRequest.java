package com.redhat.lightblue.client.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.request.execution.Execution;

public abstract class AbstractLightblueDataWithExecutionRequest extends AbstractLightblueDataRequest {

    public AbstractLightblueDataWithExecutionRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public AbstractLightblueDataWithExecutionRequest(String entityName) {
        super(entityName);
    }

    private Execution execution = null;

    public AbstractLightblueDataWithExecutionRequest execution(Execution execution) {
        this.execution = execution;
        return this;
    }

    private void appendExecutionToJson(ObjectNode node) {
        if (execution != null) {
            node.set("execution", execution.toJson());
        }
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node = (ObjectNode) super.getBodyJson();
        appendExecutionToJson(node);
        return node;
    }

}

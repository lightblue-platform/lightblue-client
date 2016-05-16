package com.redhat.lightblue.client.request.data;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataExecutionRequest;
import com.redhat.lightblue.client.util.JSON;

public class DataSaveRequest extends AbstractLightblueDataExecutionRequest {

    private Projection projection;
    private Object[] objects;
    private Boolean upsert;
    private Integer begin;
    private Integer maxResults;

    public DataSaveRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public DataSaveRequest(String entityName) {
        super(entityName);
    }

    public DataSaveRequest returns(List<? extends Projection> projection) {
        return returns(projection, null, null);
    }

    public DataSaveRequest returns(List<? extends Projection> projection, Integer begin, Integer maxResults) {
        this.projection = Projection.project(projection);
        this.begin = begin;
        this.maxResults = maxResults;

        return this;
    }

    public DataSaveRequest returns(Projection... projection) {
        return returns(projection, null, null);
    }

    public DataSaveRequest returns(Projection[] projection, Integer begin, Integer maxResults) {
        this.projection = Projection.project(projection);
        this.begin = begin;
        this.maxResults = maxResults;

        return this;
    }

    public DataSaveRequest create(Collection<?> objects) {
        create(objects.toArray());

        return this;
    }

    public DataSaveRequest create(Object... objects) {
        this.objects = objects;

        return this;
    }

    public Boolean isUpsert() {
        return upsert;
    }

    public DataSaveRequest setUpsert(Boolean upsert) {
        this.upsert = upsert;

        return this;
    }

    @Override
    public String getOperationPathParam() {
        return "save";
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node = (ObjectNode)super.getBodyJson();
        if (projection != null) {
            node.set("projection", projection.toJson());
        }
        if (objects.length == 1) {
            node.set("data", JSON.toJsonNode(objects[0]));
        } else {
            ArrayNode arr = JsonNodeFactory.instance.arrayNode();
            for (int i = 0; i < objects.length; i++) {
                arr.add(JSON.toJsonNode(objects[i]));
            }
            node.set("data", arr);
        }
        if (upsert != null) {
            node.set("upsert", JsonNodeFactory.instance.booleanNode(upsert));
        }
        appendRangeToJson(node, begin, maxResults);
        return node;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Operation getOperation() {
        return Operation.SAVE;
    }

}

package com.redhat.lightblue.client.request.data;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Range;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.util.JSON;

public class DataSaveRequest extends AbstractLightblueDataRequest {

    private Projection projection;
    private Object[] objects;
    private Boolean upsert;
    private Range range;

    public DataSaveRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public DataSaveRequest(String entityName) {
        super(entityName);
    }

    public void returns(List<? extends Projection> projection) {
        this.projection = Projection.project(projection);
    }

    public void returns(Projection... projection) {
        this.projection = Projection.project(projection);
    }

    public void create(Collection<?> objects) {
        create(objects.toArray());
    }

    public void create(Object... objects) {
        this.objects = objects;
    }

    public Boolean isUpsert() {
        return upsert;
    }

    public void setUpsert(Boolean upsert) {
        this.upsert = upsert;
    }

    public void range(Integer begin, Integer end) {
        range(new Range(begin, end));
    }

    public void range(Range range) {
        this.range = range;
    }

    @Override
    public String getOperationPathParam() {
        return "save";
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
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
        if (range != null) {
            range.appendToJson(node);
        }
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

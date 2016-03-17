package com.redhat.lightblue.client.request.data;


import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.Range;
import com.redhat.lightblue.client.Sort;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

public class DataFindRequest extends AbstractLightblueDataRequest {

    private Query queryExpression;
    private Projection projection;
    private Sort sort;
    private Range range;

    public DataFindRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public DataFindRequest(String entityName) {
        super(entityName);
    }

    public void where(Query queryExpression) {
        this.queryExpression = queryExpression;
    }

    public void select(List<? extends Projection> projections) {
        projection = Projection.project(projections);
    }

    public void select(Projection... projection) {
        this.projection = Projection.project(projection);
    }

    public void sort(List<? extends Sort> sort) {
        this.sort = Sort.sort(sort);
    }

    public void sort(Sort... sort) {
        this.sort = Sort.sort(sort);
    }

    public void range(Integer begin, Integer end) {
        range(new Range(begin, end));
    }

    public void range(Range range) {
        this.range = range;
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (queryExpression != null) {
            node.set("query", queryExpression.toJson());
        }
        if (projection != null) {
            node.set("projection", projection.toJson());
        }
        if (sort != null) {
            node.set("sort", sort.toJson());
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
    public String getOperationPathParam() {
        return "find";
    }

    @Override
    public Operation getOperation() {
        return Operation.FIND;
    }
}

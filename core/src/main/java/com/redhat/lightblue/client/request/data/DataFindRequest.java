package com.redhat.lightblue.client.request.data;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.Sort;

public class DataFindRequest extends AbstractLightblueDataRequest {

    private Query queryExpression;
    private Projection projection;
    private Sort sort;
    private Integer begin;
    private Integer end;

    public DataFindRequest() {
        super();
    }

    public DataFindRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public DataFindRequest(String entityName) {
        super(entityName);
    }

    public void where(Query queryExpression) {
        this.queryExpression = queryExpression;
    }

    @Deprecated
    public void where(com.redhat.lightblue.client.expression.query.Query queryExpression) {
        this.queryExpression = toq(queryExpression);
    }

    public void select(Projection... projection) {
        this.projection = Projection.project(projection);
    }

    @Deprecated
    public void select(com.redhat.lightblue.client.projection.Projection... projection) {
        Projection[] p = new Projection[projection.length];
        for (int i = 0; i < p.length; i++)
            p[i] = top(projection[i]);
        select(p);
    }

    @Deprecated
    public void select(Collection<com.redhat.lightblue.client.projection.Projection> projections) {
        select(projections.toArray(new com.redhat.lightblue.client.projection.Projection[projections.size()]));
    }

    @Deprecated
    public void setSortConditions(List<com.redhat.lightblue.client.request.SortCondition> sortConditions) {
        sort(sortConditions);
    }

    public void sort(Sort... sort) {
        this.sort = Sort.sort(sort);
    }

    @Deprecated
    public void sort(com.redhat.lightblue.client.request.SortCondition... sort) {
        Sort[] s = new Sort[sort.length];
        for (int i = 0; i < s.length; i++)
            s[i] = tos(sort[i]);
        sort(s);
    }

    @Deprecated
    public void sort(Collection<com.redhat.lightblue.client.request.SortCondition> sortConditions) {
        sort(sortConditions.toArray(new com.redhat.lightblue.client.request.SortCondition[sortConditions.size()]));
    }

    public void range(Integer begin, Integer end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.set("query", queryExpression.toJson());
        if (projection != null)
            node.set("projection", projection.toJson());
        if (sort != null)
            node.set("sort", sort.toJson());
        if (begin != null && end != null) {
            ArrayNode arr = JsonNodeFactory.instance.arrayNode();
            arr.add(JsonNodeFactory.instance.numberNode(begin));
            arr.add(JsonNodeFactory.instance.numberNode(end));
            node.set("range", arr);
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
    public String getOperation() {
        return Operation.FIND.toString();
    }
}

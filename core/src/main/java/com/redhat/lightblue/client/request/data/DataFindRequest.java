package com.redhat.lightblue.client.request.data;


import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.Sort;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataWithExecutionRequest;

public class DataFindRequest extends AbstractLightblueDataWithExecutionRequest {

    private Query queryExpression;
    private Projection projection;
    private Sort sort;
    private Integer begin;
    private Integer maxResults;

    public DataFindRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public DataFindRequest(String entityName) {
        super(entityName);
    }

    public DataFindRequest where(Query queryExpression) {
        this.queryExpression = queryExpression;

        return this;
    }

    public DataFindRequest select(List<? extends Projection> projections) {
        //deprecated range method might have already been called.
        return select(projections, begin, maxResults);
    }

    public DataFindRequest select(List<? extends Projection> projections, Integer begin, Integer maxResults) {
        projection = Projection.project(projections);
        this.begin = begin;
        this.maxResults = maxResults;

        return this;
    }

    public DataFindRequest select(Projection... projection) {
        //deprecated range method might have already been called.
        return select(projection, begin, maxResults);
    }

    public DataFindRequest select(Projection[] projection, Integer begin, Integer maxResults) {
        this.projection = Projection.project(projection);
        this.begin = begin;
        this.maxResults = maxResults;

        return this;
    }

    public DataFindRequest sort(List<? extends Sort> sort) {
        this.sort = Sort.sort(sort);

        return this;
    }

    public DataFindRequest sort(Sort... sort) {
        this.sort = Sort.sort(sort);

        return this;
    }

    /**
     * Use {@link #select(List, Integer, Integer)} or {@link #select(Projection[], Integer, Integer)}.
     * @param begin - the 'from' parameter to send to lightblue.
     * @param end - the 'to' parameter to send to lightblue.
     */
    @Deprecated
    public DataFindRequest range(Integer begin, Integer end) {
        this.begin = begin;
        if (end != null) {
            //'maxResults' should be 1 greater than a 'to' value.
            maxResults = end - (begin == null ? 0 : begin) + 1;
        }

        return this;
    }

    @Override
    public ObjectNode getBodyJson() {
        ObjectNode node = (ObjectNode)super.getBodyJson();
        if (queryExpression != null) {
            node.set("query", queryExpression.toJson());
        }
        if (projection != null) {
            node.set("projection", projection.toJson());
        }
        if (sort != null) {
            node.set("sort", sort.toJson());
        }
        appendRangeToJson(node, begin, maxResults);
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

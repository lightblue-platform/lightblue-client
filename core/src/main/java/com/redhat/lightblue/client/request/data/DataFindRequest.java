package com.redhat.lightblue.client.request.data;

import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.SortCondition;

import java.util.Collection;
import java.util.List;

public class DataFindRequest extends AbstractLightblueDataRequest {

    private Query queryExpression;
    private Projection[] projections;
    private SortCondition[] sortConditions;
    private Integer begin;
    private Integer end;

    public DataFindRequest() {

    }

    public DataFindRequest(String entityName, String entityVersion) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    public void where(Query queryExpression) {
        this.queryExpression = queryExpression;
    }

    public void select(Projection... projection) {
        this.projections = projection;
    }

    public void select(Collection<Projection> projections) {
        this.projections = projections.toArray(new Projection[projections.size()]);
    }

    public void setSortConditions(List<SortCondition> sortConditions) {
        this.sort(sortConditions);
    }

    public void sort(SortCondition... sortConditions) {
        this.sortConditions = sortConditions;
    }

    public void sort(Collection<SortCondition> sortConditions) {
        this.sortConditions = sortConditions.toArray(new SortCondition[sortConditions.size()]);
    }

    public void range(Integer begin, Integer end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String getBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"query\":");
        sb.append(queryExpression.toJson());
        sb.append(",\"projection\":[");
        sb.append(projections[0].toJson());

        for (int i = 1; i < projections.length; i++) {
            sb.append(",").append(projections[i].toJson());
        }

        sb.append("]");

        if (sortConditions != null && sortConditions.length > 0) {
            sb.append(",\"sort\":");
            sb.append("[");
            sb.append(sortConditions[0].toJson());

            for (int i = 1; i < sortConditions.length; i++) {
                sb.append(",").append(sortConditions[i].toJson());
            }

            sb.append("]");
        }
        if (begin != null && end != null) {
            sb.append(", \"range\": [");
            sb.append(begin);
            sb.append(",");
            sb.append(end);
            sb.append("]");
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String getOperationPathParam() {
        return Operation.FIND.getPathParam();
    }
}

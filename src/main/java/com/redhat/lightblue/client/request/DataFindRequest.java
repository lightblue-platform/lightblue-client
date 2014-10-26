package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.query.QueryExpression;
import com.redhat.lightblue.client.projection.Projection;

import java.util.Collection;
import java.util.List;

public class DataFindRequest extends AbstractLightblueRequest {

    private QueryExpression queryExpression;
    private Projection[] projections;
    private SortCondition[] sortConditions;

    public DataFindRequest() {}

    public DataFindRequest(String entityName, String entityVersion){
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

	@Override
	public RequestType getRequestType() {
		return RequestType.DATA_FIND;
	}

    public void where(QueryExpression queryExpression){
        this.queryExpression = queryExpression;
    }

    public void select(Projection... projection){
        this.projections = projection;
    }

    public void select(Collection<Projection> projections) {
        this.projections = projections.toArray( new Projection[ projections.size() ] );
    }

    public void setSortConditions(List<SortCondition> sortConditions) {
        this.sort(sortConditions);
    }

    public void sort(SortCondition... sortConditions) {
        this.sortConditions = sortConditions;
    }

    public void sort(Collection<SortCondition> sortConditions) {
        this.sortConditions = sortConditions.toArray( new SortCondition[ sortConditions.size() ] );
    }

    @Override
    public String getBody() {
        StringBuffer sb = new StringBuffer();
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

            for(int i = 1; i < sortConditions.length; i++) {
                sb.append(",").append(sortConditions[i].toJson());
            }

            sb.append("]");
        }
        sb.append("}");

        return sb.toString();
    }

}

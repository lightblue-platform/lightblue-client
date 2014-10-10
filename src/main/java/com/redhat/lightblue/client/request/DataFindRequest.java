package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.expression.Expression;
import com.redhat.lightblue.client.projection.Projection;

import java.util.List;

public class DataFindRequest extends AbstractLightblueRequest {

    private Expression expression;
    private Projection projection;
    private List<SortCondition> sortConditions;

	@Override
	public RequestType getRequestType() {
		return RequestType.DATA_FIND;
	}

    public void where(Expression expression){
        this.expression = expression;
    }

    public void select(Projection projection){
        this.projection = projection;
    }

    public void setSortConditions(List<SortCondition> sortConditions) {
        this.sort(sortConditions);
    }

    public void sort(List<SortCondition> sortConditions) {
        this.sortConditions = sortConditions;
    }

    @Override
    public String getBody() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"query\":");
        sb.append(expression.toJson());
        sb.append(",\"project\":");
        sb.append(projection.toJson());
        sb.append("}");

        return sb.toString();
    }

}

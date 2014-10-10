package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.expression.Expression;
import com.redhat.lightblue.client.projection.Projection;

public class DataFindRequest extends AbstractLightblueRequest {

    private Expression expression;
    private Projection projection;

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

    @Override
    public String getBody() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"query\":");
        sb.append(expression.toJson());
        sb.append(",\"project\":");
        sb.append(projection.toJson());
        sb.append("}");
    }

}

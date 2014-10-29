package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.expression.query.Query;

public class DataDeleteRequest extends AbstractLightblueRequest {

    private Query queryExpression;

    public DataDeleteRequest () {}

    public DataDeleteRequest(String entityName, String entityVersion){
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    public void where(Query queryExpression){
        this.queryExpression = queryExpression;
    }

	@Override
	public RequestType getRequestType() {
		return RequestType.DATA_DELETE;
	}

    @Override
    public String getBody() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"query\":");
        sb.append(queryExpression.toJson());
        sb.append("}");
        return sb.toString();
    }

}

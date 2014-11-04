package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.ExpressionOperation;

public class FieldQuery implements Query {

	protected String lefthandField;
	protected String righthandField;
	protected ExpressionOperation operation;
	
	public FieldQuery(String lefthandField, ExpressionOperation operation, String righthandField){
		this.lefthandField = lefthandField;
		this.righthandField = righthandField;
		this.operation = operation;
	}
	
	@Override
	public String toJson() {
		StringBuilder json = new StringBuilder("{");
		json.append("\"field\":\""+lefthandField+ "\",");
		json.append("\"op\":\"" +operation.toString()+ "\",");
		json.append("\"rfield\":\""+righthandField+"\"");
		json.append("}");
		return json.toString();
	}
     
}

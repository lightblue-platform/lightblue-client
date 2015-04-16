package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.ExpressionOperation;
import com.redhat.lightblue.client.enums.NaryExpressionOperation;

public class FieldQuery implements Query {
    protected String lefthandField;
    protected String righthandField;
    protected String operation;

    public FieldQuery(String lefthandField, ExpressionOperation operation, String righthandField) {
        this.lefthandField = lefthandField;
        this.righthandField = righthandField;
        this.operation = operation.toString();
    }

    public FieldQuery(String lefthandField, NaryExpressionOperation operation, String righthandField) {
        this.lefthandField = lefthandField;
        this.righthandField = righthandField;
        this.operation = operation.toString();
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"field\":\"").append(lefthandField).append("\",");
        json.append("\"op\":\"").append(operation).append("\",");
        json.append("\"rfield\":\"").append(righthandField).append("\"");
        json.append("}");
        return json.toString();
    }
}

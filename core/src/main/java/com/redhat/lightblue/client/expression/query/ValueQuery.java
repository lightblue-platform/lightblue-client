package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.ExpressionOperation;

/**
 * Created by bmiller on 10/10/14.
 */
public class ValueQuery implements Query {
    private final String field;
    private final String rValue;
    private final String operator;

    public ValueQuery(String expression) {
        String[] parts = expression.split("\\s");
        this.field = parts[0];
        this.operator = parts[1];
        this.rValue = parts[2];
    }

    public ValueQuery(String field, ExpressionOperation operation, String rValue) {
        this(field + " " + operation.toString() + " " + rValue);
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder("{\"field\":");
        json.append("\"").append(field).append("\",");
        json.append("\"op\":");
        json.append("\"").append(operator).append("\",");
        json.append("\"rvalue\":");
        json.append("\"").append(rValue).append("\"");
        json.append("}");
        return json.toString();
    }

    @Override
    public String toString() {
        return toJson();
    }

    public static ValueQuery withValue(String expression) {
        return new ValueQuery(expression);
    }
}

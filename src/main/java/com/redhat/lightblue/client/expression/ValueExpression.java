package com.redhat.lightblue.client.expression;

/**
 * Created by bmiller on 10/10/14.
 */
public class ValueExpression implements Expression {
    String field;
    String rValue;
    String operator;

    public ValueExpression(String expression) {
        String[] parts = expression.split("\\s");
        field = parts[0];
        operator = parts[1];
        rValue = parts[2];
    }

    public String toJson() {
        StringBuilder json = new StringBuilder("{ \"field\": ");
        json.append("\"").append(field).append("\", ");
        json.append("\"op\": ");
        json.append("\"").append(operator).append("\", ");
        json.append("\"rValue\": ");
        json.append("\"").append(rValue).append("\"");
        json.append(" }");
        return json.toString();
    }
}

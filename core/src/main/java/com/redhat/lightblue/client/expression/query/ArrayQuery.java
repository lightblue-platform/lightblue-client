package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.ArrayOperation;

@Deprecated
public class ArrayQuery implements Query {
    private final String fieldName;
    private ArrayOperation arrayOperation;
    private String[] array;
    private Query queryExpression;

    public ArrayQuery(String fieldName, Query queryExpression) {
        this.fieldName = fieldName;
        this.queryExpression = queryExpression;
    }

    public ArrayQuery(String fieldName, ArrayOperation operation, String... values) {
        this.fieldName = fieldName;
        this.arrayOperation = operation;
        this.array = values;
    }

    @Override
    public String toJson() {
        StringBuilder builder = new StringBuilder("{");
        if (queryExpression != null) {
            builder.append("\"array\":\"").append(fieldName).append("\",");
            builder.append("\"elemMatch\":");
            builder.append(queryExpression.toJson());
        } else {
            builder.append("\"array\":\"").append(fieldName).append("\",");
            builder.append("\"contains\":\"").append(arrayOperation.toString()).append("\",");
            builder.append("\"values\":[");
            builder.append("");

            builder.append("\"").append(array[0]).append("\"");

            if (array.length > 1) {
                for (int i = 1; i < array.length; i++) {
                    builder.append(",\"").append(array[i]).append("\"");
                }
            }

            builder.append("]");

        }
        builder.append("}");
        return builder.toString();
    }

    public static ArrayQuery withSubfield(String fieldName, Query queryExpression) {
        return new ArrayQuery(fieldName, queryExpression);
    }

    public static ArrayQuery withSubfield(String fieldName, ArrayOperation operation, String... values) {
        return new ArrayQuery(fieldName, operation, values);
    }

}

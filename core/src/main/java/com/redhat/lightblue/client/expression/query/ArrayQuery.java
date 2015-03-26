/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.ArrayOperation;

public class ArrayQuery implements Query {
    private final String fieldName;
    private ArrayOperation arrayOperation;
    private String[] array;
    private Query queryExpression;

    public ArrayQuery(String fieldName, Query queryExpression) {
        this.fieldName = fieldName;
        this.queryExpression = queryExpression;
    }

    ArrayQuery(String fieldName, ArrayOperation operation, String... values) {
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
}

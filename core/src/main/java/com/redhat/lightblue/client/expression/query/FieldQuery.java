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

import com.redhat.lightblue.client.enums.ExpressionOperation;

public class FieldQuery implements Query {
    protected String lefthandField;
    protected String righthandField;
    protected ExpressionOperation operation;

    public FieldQuery(String lefthandField, ExpressionOperation operation, String righthandField) {
        this.lefthandField = lefthandField;
        this.righthandField = righthandField;
        this.operation = operation;
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"field\":\"").append(lefthandField).append("\",");
        json.append("\"op\":\"").append(operation.toString()).append("\",");
        json.append("\"rfield\":\"").append(righthandField).append("\"");
        json.append("}");
        return json.toString();
    }
}

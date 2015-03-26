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

import com.redhat.lightblue.client.enums.UnaryOperation;

/**
 * created by Michael White 10/10/2014
 */
public class UnaryLogicalQuery implements Query {
    private final UnaryOperation operation;
    private final Query queryExpression;

    // used for NOT
    public UnaryLogicalQuery(UnaryOperation operation, Query queryExpression) {
        this.operation = operation;
        this.queryExpression = queryExpression;
    }

    @Override
    public String toJson() throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"");
        builder.append(this.operation.toString());
        builder.append("\":");
        builder.append(this.queryExpression.toJson());
        builder.append("}");

        return builder.toString();
    }

    public static UnaryLogicalQuery not(Query queryExpression) {
        return new UnaryLogicalQuery(UnaryOperation.NOT, queryExpression);
    }
}

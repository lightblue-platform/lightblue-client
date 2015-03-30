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

import com.redhat.lightblue.client.enums.NaryOperation;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */
public class NaryLogicalQuery implements Query {
    private final NaryOperation operation;
    private final Query[] queryExpressions;

    // used for AND, OR  
    // tbd: require 2+ at sig level, or throw exception if called on less than 2?  
    public NaryLogicalQuery(NaryOperation operation, Query... queryExpressions) {
        this.operation = operation;
        this.queryExpressions = queryExpressions;
    }

    // used for AND, OR  
    // TO DO throw exception on less than 2 expressions in list  
    public NaryLogicalQuery(NaryOperation operation, Collection<Query> queryExpressions) {
        this.operation = operation;
        this.queryExpressions = queryExpressions.toArray(new Query[queryExpressions.size()]);
    }

    @Override
    public String toJson() throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"");
        builder.append(this.operation.toString());
        builder.append("\":[");
        for (int index = 0; index < this.queryExpressions.length; index++) {
            builder.append(this.queryExpressions[index].toJson());
            // if there's more than one element left...
            if ((this.queryExpressions.length - index) > 1) {
                builder.append(","); // append a comma
            }
        }
        builder.append("]}");

        return builder.toString();
    }

    public static NaryLogicalQuery and(Query... queryExpressions) {
        return new NaryLogicalQuery(NaryOperation.AND, queryExpressions);
    }

    public static NaryLogicalQuery and(Collection<Query> queryExpressions) {
        return new NaryLogicalQuery(NaryOperation.AND, queryExpressions);
    }

    public static NaryLogicalQuery or(Query... queryExpressions) {
        return new NaryLogicalQuery(NaryOperation.OR, queryExpressions);
    }

    public static NaryLogicalQuery or(Collection<Query> queryExpressions) {
        return new NaryLogicalQuery(NaryOperation.OR, queryExpressions);
    }
}

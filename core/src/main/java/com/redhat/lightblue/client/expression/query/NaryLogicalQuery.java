package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.NaryOperation;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */
@Deprecated
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

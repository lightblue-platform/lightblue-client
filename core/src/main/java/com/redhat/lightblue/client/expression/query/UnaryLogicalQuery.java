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
        StringBuffer builder = new StringBuffer();

        builder.append("{\"");
        builder.append( this.operation.toString() );
        builder.append("\":");
        builder.append( this.queryExpression.toJson() );
        builder.append("}");

        return builder.toString();
    }

    public static UnaryLogicalQuery not(Query queryExpression){
        return new UnaryLogicalQuery(UnaryOperation.NOT, queryExpression);
    }
}

package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.UnaryOperation;


import java.lang.StringBuffer;

/**
 * created by Michael White 10/10/2014
 */

public class UnaryLogicalQuery implements Query {
    
    private UnaryOperation operation;
    private Query queryExpression;
    
    // used for NOT
    public UnaryLogicalQuery(UnaryOperation operation, Query queryExpression) {
        this.operation = operation;
        this.queryExpression = queryExpression;
    }
    
    public String toJson() throws IllegalArgumentException {
        StringBuffer builder = new StringBuffer();
        
        builder.append("{\"");
        builder.append( this.operation.toString() );
        builder.append("\":");
        builder.append( this.queryExpression.toJson() );
        builder.append("}");
        
        return builder.toString();
    }
}
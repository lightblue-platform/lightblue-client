package com.redhat.lightblue.client.query;

import com.redhat.lightblue.client.enums.UnaryOperation;


import java.lang.StringBuffer;

/**
 * created by Michael White 10/10/2014
 */

public class UnaryLogicalQueryExpression implements QueryExpression {
    
    private UnaryOperation operation;
    private QueryExpression queryExpression;
    
    // used for NOT
    public UnaryLogicalQueryExpression(UnaryOperation operation, QueryExpression queryExpression) {
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
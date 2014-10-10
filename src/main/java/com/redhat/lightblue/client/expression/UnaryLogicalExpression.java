package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.UnaryOperation;
import com.redhat.lightblue.client.expression.Expression;



import java.lang.StringBuffer;

/**
 * created by Michael White 10/09/2014
 */

public class UnaryLogicalExpression implements Expression {
    
    private UnaryOperation operation;
    private Expression expression;
    
    // used for NOT
    public UnaryLogicalExpression( UnaryOperation operation, Expression expression ) {
        this.operation = operation;
        this.expression = expression;
    }
    
    public String toJson() throws IllegalArgumentException {
        StringBuffer builder = new StringBuffer();
        
        builder.append("{\"");
        builder.append( this.operation.toString() );
        builder.append("\":");
        builder.append( this.expression.toJson() );
        builder.append("}");
        
        return builder.toString();
    }
}
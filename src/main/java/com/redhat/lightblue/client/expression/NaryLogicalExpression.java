package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.NaryOperation;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class NaryLogicalExpression implements Expression {
    
    private NaryOperation operation;
    private Expression[] expressions;
    
    // used for AND, OR  
    // tbd: require 2+ at sig level, or throw exception if called on less than 2?  
    public NaryLogicalExpression( NaryOperation operation, Expression... expressions ) {
        this.operation = operation;
        this.expressions = expressions;
    }
    
    // used for AND, OR  
    // TO DO throw exception on less than 2 expressions in list  
    public NaryLogicalExpression( NaryOperation operation, Collection<Expression> expressions ) {
        this.operation = operation;
        this.expressions = expressions.toArray( new Expression[ expressions.size()] );
    }
    
    public String toJson() throws IllegalArgumentException {
        StringBuffer builder = new StringBuffer();
        
        builder.append("{\"");
        builder.append( this.operation.toString() );
        builder.append("\":[");
        for( int index = 0; index < this.expressions.length; index++ ){
            builder.append( this.expressions[index].toJson() );
            // if there's more than one element left...
            if( ( this.expressions.length - index ) > 1 ){
                builder.append(","); // append a comma
            }
        }
        builder.append("]}");
        
        return builder.toString();
    }

    public static NaryLogicalExpression and(Expression... expressions){
        return new NaryLogicalExpression(NaryOperation.AND, expressions);
    }

    public static NaryLogicalExpression and(Collection<Expression> expressions){
        return new NaryLogicalExpression(NaryOperation.AND, expressions);
    }

    public static NaryLogicalExpression or(Expression... expressions){
        return new NaryLogicalExpression(NaryOperation.OR, expressions);
    }

    public static NaryLogicalExpression or(Collection<Expression> expressions){
        return new NaryLogicalExpression(NaryOperation.OR, expressions);
    }
}
package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.NaryOperation;
import com.redhat.lightblue.client.expression.Expression;



import java.lang.StringBuffer;
import java.util.Collection;

/**
 * created by Michael White 10/09/2014
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
        
        builder.append("{ \"");
        // there's only two NARY operators, but....
        if( this.operation.equals(NaryOperation.AND) ) {
            builder.append("$and");
        }else if ( this.operation.equals(NaryOperation.OR) ){
            builder.append("$or");
        }else{
            throw new IllegalArgumentException("This NARY operator is not yet supported.");
        }
        builder.append("\" : [ ");
        for( int index = 0; index < this.expressions.length; index++ ){
            builder.append( this.expressions[index].toJson() );
            // if there's more than one element left...
            if( ( this.expressions.length - index ) > 1 ){
                builder.append(", "); // append a comma
            }
        }
        builder.append(" ] }");
        
        return builder.toString();
    }
}
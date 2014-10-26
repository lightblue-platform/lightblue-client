package com.redhat.lightblue.client.query;

import com.redhat.lightblue.client.enums.NaryOperation;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class NaryLogicalQueryExpression implements QueryExpression {
    
    private NaryOperation operation;
    private QueryExpression[] queryExpressions;
    
    // used for AND, OR  
    // tbd: require 2+ at sig level, or throw exception if called on less than 2?  
    public NaryLogicalQueryExpression(NaryOperation operation, QueryExpression... queryExpressions) {
        this.operation = operation;
        this.queryExpressions = queryExpressions;
    }
    
    // used for AND, OR  
    // TO DO throw exception on less than 2 expressions in list  
    public NaryLogicalQueryExpression(NaryOperation operation, Collection<QueryExpression> queryExpressions) {
        this.operation = operation;
        this.queryExpressions = queryExpressions.toArray( new QueryExpression[ queryExpressions.size()] );
    }
    
    public String toJson() throws IllegalArgumentException {
        StringBuffer builder = new StringBuffer();
        
        builder.append("{\"");
        builder.append( this.operation.toString() );
        builder.append("\":[");
        for( int index = 0; index < this.queryExpressions.length; index++ ){
            builder.append( this.queryExpressions[index].toJson() );
            // if there's more than one element left...
            if( ( this.queryExpressions.length - index ) > 1 ){
                builder.append(","); // append a comma
            }
        }
        builder.append("]}");
        
        return builder.toString();
    }

    public static NaryLogicalQueryExpression and(QueryExpression... queryExpressions){
        return new NaryLogicalQueryExpression(NaryOperation.AND, queryExpressions);
    }

    public static NaryLogicalQueryExpression and(Collection<QueryExpression> queryExpressions){
        return new NaryLogicalQueryExpression(NaryOperation.AND, queryExpressions);
    }

    public static NaryLogicalQueryExpression or(QueryExpression... queryExpressions){
        return new NaryLogicalQueryExpression(NaryOperation.OR, queryExpressions);
    }

    public static NaryLogicalQueryExpression or(Collection<QueryExpression> queryExpressions){
        return new NaryLogicalQueryExpression(NaryOperation.OR, queryExpressions);
    }
}
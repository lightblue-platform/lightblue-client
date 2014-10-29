package com.redhat.lightblue.client.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class InsertUpdateExpression implements UpdateExpression {
    
    private String path;
    private Integer index;
    private RValueExpression[] expressions;
    
    public InsertUpdateExpression( String path, Integer index, RValueExpression... expressions ){
        this.path = path;
        this.index = index;
        this.expressions = expressions;
    }
    
    public InsertUpdateExpression( String path, Integer index, Collection<RValueExpression> expressions ){
        this.path = path;
        this.index = index;
        this.expressions = expressions.toArray( new RValueExpression[ expressions.size() ] );
    }
    
    public String toJson() {
        
        /*
         * { $insert : { path : rvalue_expression } }
         * { $insert : { path : [ rvalue_expression,...] }}
         */
        StringBuilder json = new StringBuilder("{");
        json.append("$insert:{");
        json.append(this.path+"."+this.index.toString()+":");
        if( expressions.length > 1 ){
            json.append("[");
        }
        for(int index=0; index<expressions.length;index++){
            json.append(expressions[index].toJson());
            if( ( this.expressions.length - index ) > 1 ){
                json.append(","); // append a comma
            }
        }
        if( expressions.length > 1 ){
            json.append("]");
        }
        json.append("}");
        return json.toString();
    }
    
}
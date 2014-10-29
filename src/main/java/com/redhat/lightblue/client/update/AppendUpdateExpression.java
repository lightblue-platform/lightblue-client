package com.redhat.lightblue.client.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class AppendUpdateExpression implements UpdateExpression {
    
    private String path;
    private RValueExpression[] expressions;
    
    public AppendUpdateExpression( String path, RValueExpression... expressions ){
        this.path = path;
        this.expressions = expressions;
    }
    
    public AppendUpdateExpression( String path, Collection<RValueExpression> expressions ){
        this.path = path;
        this.expressions = expressions.toArray( new RValueExpression[ expressions.size() ]);
    }
    
    public String toJson() {
        /*
         * { "$append" : { pathToArray : [ values ] } } (values can be empty objects (extend array with a  new element)
         * { "$append" : { pathToArray : value } }
         */
        StringBuilder json = new StringBuilder("{");
        json.append("$append:{");
        json.append(this.path);
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
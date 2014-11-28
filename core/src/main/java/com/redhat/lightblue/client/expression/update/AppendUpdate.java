package com.redhat.lightblue.client.expression.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class AppendUpdate implements Update {
    
    private String path;
    private RValue[] expressions;
    
    public AppendUpdate( String path, RValue... expressions ){
        this.path = path;
        this.expressions = expressions;
    }
    
    public AppendUpdate( String path, Collection<RValue> expressions ){
        this.path = path;
        this.expressions = expressions.toArray( new RValue[ expressions.size() ]);
    }
    
    public String toJson() {
        /*
         * { "$append" : { pathToArray : [ values ] } } (values can be empty objects (extend array with a  new element)
         * { "$append" : { pathToArray : value } }
         */
        StringBuilder json = new StringBuilder("{");
        json.append("\"$append\":{");
        json.append("\""+this.path+"\":");
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
        json.append("}"); // close append
        json.append("}"); // close main block
        return json.toString();
    }
    
}
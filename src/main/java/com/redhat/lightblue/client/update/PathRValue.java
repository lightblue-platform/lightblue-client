package com.redhat.lightblue.client.update;


/**
 * created by Michael White 10/10/2014
 */

public class PathRValue implements RValueExpression {
    
    private String path;
    
    public PathRValue( String path ) {
        this.path = path;
    }
    
    public String toJson(){
        return "{$valueof:"+this.path+"}";
    }
    
}
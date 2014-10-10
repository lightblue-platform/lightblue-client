package com.redhat.lightblue.client.update;

/**
 * created by Michael White 10/10/2014
 */

public class PathValuePair {
    
    public final String path;
    public final RValueExpression value;
    
    public PathValuePair( String path, RValueExpression value ) {
        this.path = path;
        this.value = value;
    }
    
    public String toJson() {
        StringBuffer builder = new StringBuffer();
        
        builder.append("{path:");
        builder.append( value.toJson() );
        builder.append("}");
        
        return builder.toString();
    }
    
}
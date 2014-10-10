package com.redhat.lightblue.client.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class AppendUpdateExpression implements UpdateExpression {
    
    public AppendUpdateExpression( String path, RValueExpression... expressions ){
        
    }
    
    public AppendUpdateExpression( String path, Collection<RValueExpression> expressions ){
        
    }
    
    public String toJson() {
        return "";
    }
    
}
package com.redhat.lightblue.client.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class InsertUpdateExpression implements UpdateExpression {
    
    public InsertUpdateExpression( String path, Integer index, RValueExpression... expressions ){
        
    }
    
    public InsertUpdateExpression( String path, Integer index, Collection<RValueExpression> expressions ){
        
    }
    
    public String toJson() {
        return "";
    }
    
}
package com.redhat.lightblue.client.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class SetUpdateExpression implements UpdateExpression {
    
    public SetUpdateExpression(PathValuePair... statements ){
        
    }
    
    public SetUpdateExpression(Collection<PathValuePair> statements ){
        
    }
    
    public String toJson() {
        return "";
    }
    
}
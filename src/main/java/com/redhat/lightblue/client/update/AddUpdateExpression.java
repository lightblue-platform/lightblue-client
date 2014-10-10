package com.redhat.lightblue.client.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class AddUpdateExpression implements UpdateExpression {
    
    public AddUpdateExpression(PathValuePair... statements ){
        
    }
    
    public AddUpdateExpression(Collection<PathValuePair> statements ){
        
    }
    
    public String toJson() {
        return "";
    }
    
}
package com.redhat.lightblue.client.expression.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class SetUpdate implements Update {
    
	PathValuePair[] pathValuePairs;
	
    public SetUpdate(PathValuePair... statements ){
    	pathValuePairs = statements;
    }
    
    public SetUpdate(Collection<PathValuePair> statements ){
    	pathValuePairs = (PathValuePair[])statements.toArray();
    }
    
    public String toJson() {
    	StringBuilder json = new StringBuilder("{");
    	json.append("$set:{");
    	for(int index=0; index<pathValuePairs.length;index++){
    		json.append(pathValuePairs[index].toJson());
    		if( ( this.pathValuePairs.length - index ) > 1 ){
    		    json.append(", ");
    		}
    	}
    	json.append("}}");
        return json.toString();
    }
    
}
package com.redhat.lightblue.client.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class SetUpdateExpression implements UpdateExpression {
    
	PathValuePair[] pathValuePairs;
	
    public SetUpdateExpression(PathValuePair... statements ){
    	pathValuePairs = statements;
    }
    
    public SetUpdateExpression(Collection<PathValuePair> statements ){
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
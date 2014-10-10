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
    	json.append(pathValuePairs[0].path+":"+pathValuePairs[0].value.toJson());
    	for(int i=1; i<pathValuePairs.length;i++){
    		json.append(","+pathValuePairs[i].path+":"+pathValuePairs[i].value.toJson());
    	}
    	json.append("}}");
        return json.toString();
    }
    
}
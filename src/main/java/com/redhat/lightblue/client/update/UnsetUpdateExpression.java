package com.redhat.lightblue.client.update;

/**
 * created by Michael White 10/10/2014
 */

public class UnsetUpdateExpression implements UpdateExpression {
    
    private String[] paths;
    
    public UnsetUpdateExpression(String... paths ){
        this.paths = paths;
    }
    
    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("$unset:{");
        if( paths.length > 1 ){
            json.append("[");
        }
        for(int index=0; index<paths.length;index++){
            json.append(paths[index]);
            if( ( this.paths.length - index ) > 1 ){
                json.append(","); // append a comma
            }
        }
        if( paths.length > 1 ){
            json.append("]");
        }
        json.append("}");
        return json.toString();
    }
    
}
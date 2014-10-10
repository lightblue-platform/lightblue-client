package com.redhat.lightblue.client.update;


/**
 * created by Michael White 10/10/2014
 */

public class NullRValue implements RValueExpression {

    public String toJson(){
        return "{}";
    }
    
}
package com.redhat.lightblue.client.expression.update;


/**
 * created by Michael White 10/10/2014
 */

public class NullRValue implements RValue {

    public String toJson(){
        return "{}";
    }
    
}
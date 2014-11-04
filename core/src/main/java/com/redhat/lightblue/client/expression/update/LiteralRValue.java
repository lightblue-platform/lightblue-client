package com.redhat.lightblue.client.expression.update;


/**
 * created by Michael White 10/10/2014
 */

public class LiteralRValue implements RValue {
    
    private String value;
    
    public LiteralRValue( String value ) {
        this.value = value;
    }
    
    public String toJson(){
        return this.value;
    }
    
}
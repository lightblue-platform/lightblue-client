package com.redhat.lightblue.client.update;


/**
 * created by Michael White 10/10/2014
 */

public class LiteralRValue implements RValueExpression {
    
    private String value;
    
    public LiteralRValue( String value ) {
        this.value = value;
    }
    
    public String toJson(){
        return this.value;
    }
    
}
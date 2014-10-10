package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.UnaryOperation;

import org.junit.Test;
import org.junit.Assert;

/**
 * Created by Michael White on 10/10/14.
 */
public class UnaryLogicalExpressionTest {

    private Expression testExpression = new Expression(){
        public String toJson(){
            return "{\"field\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };
    
    @Test
    public void testToJson(){
        Expression expression = new UnaryLogicalExpression( UnaryOperation.NOT, testExpression );
        String expectedJson = "{\"$not\":"+testExpression.toJson()+"}";
        Assert.assertEquals( expectedJson, expression.toJson() );
    }
}

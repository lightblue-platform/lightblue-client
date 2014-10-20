package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.UnaryOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

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
    public void testToJson() throws JSONException {
        Expression expression = new UnaryLogicalExpression( UnaryOperation.NOT, testExpression );
        String expectedJson = "{\"$not\":"+testExpression.toJson()+"}";
        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }
}

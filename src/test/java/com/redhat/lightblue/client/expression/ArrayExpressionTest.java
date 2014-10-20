package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.ArrayOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by vkumar on 10/10/14.
 */
public class ArrayExpressionTest {

    @Test
    public void testToJsonConstructedWithObjectElementExpression() throws JSONException {
    	Expression expression = new Expression() {
			
			@Override
			public String toJson() {
				String json= "{\"field\":\"item\",\"op\":\"=\",\"rvalue\":\"1\"}";
				return json;
			}
		};
		ArrayExpression arrayExpression = new ArrayExpression("someArray", expression);
         String expectedJson = "{\"array\":\"someArray\",\"elemMatch\":{\"field\":\"item\",\"op\":\"=\",\"rvalue\":\"1\"}}";

        JSONAssert.assertEquals(expectedJson, arrayExpression.toJson(), false);
    }

    
    @Test
    public void testToJsonConstructedWithcontainsvalue1andvalue2Expression() throws JSONException {
    	String expectedJson ="{\"array\":\"someArray\",\"contains\":\"$all\",\"values\":[\"value1\",\"value2\",\"value3\"]}";

    	String[] array = {"value1", "value2", "value3"};
		ArrayExpression arrayExpression = new ArrayExpression("someArray", ArrayOperation.ALL, array);
      
        JSONAssert.assertEquals(expectedJson, arrayExpression.toJson(), false);
    }
    
}

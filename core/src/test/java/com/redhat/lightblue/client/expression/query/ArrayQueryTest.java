package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.ArrayOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by vkumar on 10/10/14.
 */
public class ArrayQueryTest {

    @Test
    public void testToJsonConstructedWithObjectElementExpression() throws JSONException {
    	Query queryExpression = new Query() {
			
			@Override
			public String toJson() {
				String json= "{\"field\":\"item\",\"op\":\"=\",\"rvalue\":\"1\"}";
				return json;
			}
		};
		ArrayQuery arrayExpression = new ArrayQuery("someArray", queryExpression);
         String expectedJson = "{\"array\":\"someArray\",\"elemMatch\":{\"field\":\"item\",\"op\":\"=\",\"rvalue\":\"1\"}}";

        JSONAssert.assertEquals(expectedJson, arrayExpression.toJson(), false);
    }

    
    @Test
    public void testToJsonConstructedWithcontainsvalue1andvalue2Expression() throws JSONException {
    	String expectedJson ="{\"array\":\"someArray\",\"contains\":\"$all\",\"values\":[\"value1\",\"value2\",\"value3\"]}";

    	String[] array = {"value1", "value2", "value3"};
		ArrayQuery arrayExpression = new ArrayQuery("someArray", ArrayOperation.ALL, array);
      
        JSONAssert.assertEquals(expectedJson, arrayExpression.toJson(), false);
    }
    
}

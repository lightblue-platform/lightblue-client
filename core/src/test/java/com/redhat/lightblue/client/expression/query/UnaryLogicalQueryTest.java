package com.redhat.lightblue.client.expression.query;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by Michael White on 10/10/14.
 */
public class UnaryLogicalQueryTest {

    private final Query testQueryExpression = new Query(){
        @Override
        public String toJson(){
            return "{\"field\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };

    @Test
    public void testNot() throws JSONException{
        UnaryLogicalQuery queryExpression = UnaryLogicalQuery.not(testQueryExpression);
        String expectedJson = "{\"$not\":"+ testQueryExpression.toJson()+"}";
        JSONAssert.assertEquals(expectedJson, queryExpression.toJson(), false);
    }
}

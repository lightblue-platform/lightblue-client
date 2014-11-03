package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.UnaryOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by Michael White on 10/10/14.
 */
public class UnaryLogicalQueryTest {

    private Query testQueryExpression = new Query(){
        public String toJson(){
            return "{\"field\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };
    
    @Test
    public void testToJson() throws JSONException {
        Query queryExpression = new UnaryLogicalQuery( UnaryOperation.NOT, testQueryExpression);
        String expectedJson = "{\"$not\":"+ testQueryExpression.toJson()+"}";
        JSONAssert.assertEquals(expectedJson, queryExpression.toJson(), false);
    }
}

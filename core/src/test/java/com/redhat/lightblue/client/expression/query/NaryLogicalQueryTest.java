package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.NaryOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael White on 10/10/14.
 */
public class NaryLogicalQueryTest {

    private Query testQueryExpression = new Query(){
        public String toJson(){
            return "{\"field\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };
    
    @Test
    public void testToJsonOneOperationArrayConstructor() throws JSONException {
        
        NaryLogicalQuery expression = new NaryLogicalQuery( NaryOperation.AND, testQueryExpression, testQueryExpression);
        String expectedJson = "{\"$and\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQuery( NaryOperation.OR, testQueryExpression, testQueryExpression);
        expectedJson = "{\"$or\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQuery( NaryOperation.ANY, testQueryExpression, testQueryExpression);
        expectedJson = "{\"$any\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQuery( NaryOperation.ALL, testQueryExpression, testQueryExpression);
        expectedJson = "{\"$all\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
    }
    
    @Test
    public void testToJsonOneOperationCollectionConstructor() throws JSONException {
        
        List<Query> queryExpressions = new ArrayList<>();
        queryExpressions.add(testQueryExpression);
        queryExpressions.add(testQueryExpression);
        
        NaryLogicalQuery expression = new NaryLogicalQuery( NaryOperation.AND, queryExpressions);
        String expectedJson = "{\"$and\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQuery( NaryOperation.OR, queryExpressions);
        expectedJson = "{\"$or\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQuery( NaryOperation.ANY, queryExpressions);
        expectedJson = "{\"$any\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQuery( NaryOperation.ALL, queryExpressions);
        expectedJson = "{\"$all\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
    }
    
    @Test
    public void testToJsonTwoOperations() throws JSONException {
        
        List<Query> queryExpressions = new ArrayList<>();
        queryExpressions.add(testQueryExpression);
        queryExpressions.add(testQueryExpression);
        
        NaryLogicalQuery expression = new NaryLogicalQuery( NaryOperation.OR, queryExpressions);
        expression = new NaryLogicalQuery( NaryOperation.AND, expression, testQueryExpression);
        String expectedJson = "{\"$and\":[{\"$or\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]},"+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
    }
}

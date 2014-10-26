package com.redhat.lightblue.client.query;

import com.redhat.lightblue.client.enums.NaryOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael White on 10/10/14.
 */
public class NaryLogicalQueryExpressionTest {

    private QueryExpression testQueryExpression = new QueryExpression(){
        public String toJson(){
            return "{\"field\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };
    
    @Test
    public void testToJsonOneOperationArrayConstructor() throws JSONException {
        
        NaryLogicalQueryExpression expression = new NaryLogicalQueryExpression( NaryOperation.AND, testQueryExpression, testQueryExpression);
        String expectedJson = "{\"$and\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQueryExpression( NaryOperation.OR, testQueryExpression, testQueryExpression);
        expectedJson = "{\"$or\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQueryExpression( NaryOperation.ANY, testQueryExpression, testQueryExpression);
        expectedJson = "{\"$any\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQueryExpression( NaryOperation.ALL, testQueryExpression, testQueryExpression);
        expectedJson = "{\"$all\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
    }
    
    @Test
    public void testToJsonOneOperationCollectionConstructor() throws JSONException {
        
        List<QueryExpression> queryExpressions = new ArrayList<>();
        queryExpressions.add(testQueryExpression);
        queryExpressions.add(testQueryExpression);
        
        NaryLogicalQueryExpression expression = new NaryLogicalQueryExpression( NaryOperation.AND, queryExpressions);
        String expectedJson = "{\"$and\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQueryExpression( NaryOperation.OR, queryExpressions);
        expectedJson = "{\"$or\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQueryExpression( NaryOperation.ANY, queryExpressions);
        expectedJson = "{\"$any\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalQueryExpression( NaryOperation.ALL, queryExpressions);
        expectedJson = "{\"$all\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
    }
    
    @Test
    public void testToJsonTwoOperations() throws JSONException {
        
        List<QueryExpression> queryExpressions = new ArrayList<>();
        queryExpressions.add(testQueryExpression);
        queryExpressions.add(testQueryExpression);
        
        NaryLogicalQueryExpression expression = new NaryLogicalQueryExpression( NaryOperation.OR, queryExpressions);
        expression = new NaryLogicalQueryExpression( NaryOperation.AND, expression, testQueryExpression);
        String expectedJson = "{\"$and\":[{\"$or\":["+ testQueryExpression.toJson()+","+ testQueryExpression.toJson()+"]},"+ testQueryExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
    }
}

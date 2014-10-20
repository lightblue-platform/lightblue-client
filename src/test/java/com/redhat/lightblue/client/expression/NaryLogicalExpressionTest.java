package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.NaryOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael White on 10/10/14.
 */
public class NaryLogicalExpressionTest {

    private Expression testExpression = new Expression(){
        public String toJson(){
            return "{\"field\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };
    
    @Test
    public void testToJsonOneOperationArrayConstructor() throws JSONException {
        
        NaryLogicalExpression expression = new NaryLogicalExpression( NaryOperation.AND, testExpression, testExpression );
        String expectedJson = "{\"$and\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalExpression( NaryOperation.OR, testExpression, testExpression );
        expectedJson = "{\"$or\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalExpression( NaryOperation.ANY, testExpression, testExpression );
        expectedJson = "{\"$any\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalExpression( NaryOperation.ALL, testExpression, testExpression );
        expectedJson = "{\"$all\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
    }
    
    @Test
    public void testToJsonOneOperationCollectionConstructor() throws JSONException {
        
        List<Expression> expressions = new ArrayList<>();
        expressions.add(testExpression);
        expressions.add(testExpression);
        
        NaryLogicalExpression expression = new NaryLogicalExpression( NaryOperation.AND, expressions );
        String expectedJson = "{\"$and\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalExpression( NaryOperation.OR, expressions );
        expectedJson = "{\"$or\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalExpression( NaryOperation.ANY, expressions );
        expectedJson = "{\"$any\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
        
        expression = new NaryLogicalExpression( NaryOperation.ALL, expressions );
        expectedJson = "{\"$all\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
    }
    
    @Test
    public void testToJsonTwoOperations() throws JSONException {
        
        List<Expression> expressions = new ArrayList<>();
        expressions.add(testExpression);
        expressions.add(testExpression);
        
        NaryLogicalExpression expression = new NaryLogicalExpression( NaryOperation.OR, expressions );
        expression = new NaryLogicalExpression( NaryOperation.AND, expression, testExpression );
        String expectedJson = "{\"$and\":[{\"$or\":["+testExpression.toJson()+","+testExpression.toJson()+"]},"+testExpression.toJson()+"]}";
        JSONAssert.assertEquals( expectedJson, expression.toJson(), false);
    }
}

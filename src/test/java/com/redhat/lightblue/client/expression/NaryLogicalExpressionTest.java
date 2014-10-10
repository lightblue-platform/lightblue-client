package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.NaryOperation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

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
    public void testToJsonOneOperationArrayConstructor(){
        
        NaryLogicalExpression expression = new NaryLogicalExpression( NaryOperation.AND, testExpression, testExpression );
        String expectedJson = "{\"$and\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        Assert.assertEquals( expectedJson, expression.toJson() );
        
        expression = new NaryLogicalExpression( NaryOperation.OR, testExpression, testExpression );
        expectedJson = "{\"$or\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        Assert.assertEquals( expectedJson, expression.toJson() );
        
        expression = new NaryLogicalExpression( NaryOperation.ANY, testExpression, testExpression );
        expectedJson = "{\"$any\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        Assert.assertEquals( expectedJson, expression.toJson() );
        
        expression = new NaryLogicalExpression( NaryOperation.ALL, testExpression, testExpression );
        expectedJson = "{\"$all\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        Assert.assertEquals( expectedJson, expression.toJson() );
    }
    
    @Test
    public void testToJsonOneOperationCollectionConstructor(){
        
        List<Expression> expressions = new ArrayList<>();
        expressions.add(testExpression);
        expressions.add(testExpression);
        
        NaryLogicalExpression expression = new NaryLogicalExpression( NaryOperation.AND, expressions );
        String expectedJson = "{\"$and\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        Assert.assertEquals( expectedJson, expression.toJson() );
        
        expression = new NaryLogicalExpression( NaryOperation.OR, expressions );
        expectedJson = "{\"$or\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        Assert.assertEquals( expectedJson, expression.toJson() );
        
        expression = new NaryLogicalExpression( NaryOperation.ANY, expressions );
        expectedJson = "{\"$any\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        Assert.assertEquals( expectedJson, expression.toJson() );
        
        expression = new NaryLogicalExpression( NaryOperation.ALL, expressions );
        expectedJson = "{\"$all\":["+testExpression.toJson()+","+testExpression.toJson()+"]}";
        Assert.assertEquals( expectedJson, expression.toJson() );
    }
    
    @Test
    public void testToJsonTwoOperations(){
        
        List<Expression> expressions = new ArrayList<>();
        expressions.add(testExpression);
        expressions.add(testExpression);
        
        NaryLogicalExpression expression = new NaryLogicalExpression( NaryOperation.OR, expressions );
        expression = new NaryLogicalExpression( NaryOperation.AND, expression, testExpression );
        String expectedJson = "{\"$and\":[{\"$or\":["+testExpression.toJson()+","+testExpression.toJson()+"]},"+testExpression.toJson()+"]}";
        Assert.assertEquals( expectedJson, expression.toJson() );
    }
}

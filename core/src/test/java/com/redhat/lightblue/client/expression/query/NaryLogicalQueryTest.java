/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

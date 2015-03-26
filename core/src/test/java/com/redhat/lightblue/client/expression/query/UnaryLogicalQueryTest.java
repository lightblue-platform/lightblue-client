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

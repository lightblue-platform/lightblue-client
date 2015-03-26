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

import com.redhat.lightblue.client.enums.ExpressionOperation;

/**
 * Created by bmiller on 10/10/14.
 */
public class ValueQueryTest {

    @Test
    public void testToJsonConstructedWithStringExpression() throws JSONException {
        ValueQuery expression = new ValueQuery("field1 = value1");
        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rvalue\":\"value1\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() throws JSONException {
        ValueQuery expression = new ValueQuery("field1", ExpressionOperation.EQUALS, "value1");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rvalue\":\"value1\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testToStringCallsToJson() throws JSONException {
        ValueQuery expression = new ValueQuery("field1 = value1");

        JSONAssert.assertEquals(expression.toString(), expression.toJson(), false);
    }

    @Test
    public void testValueWithWhiteSpace() throws JSONException {
        ValueQuery expression = new ValueQuery("field1 = Red Hat Enterprise Linux");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rvalue\":\"Red Hat Enterprise Linux\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testFieldInArray() throws JSONException {
        ValueQuery expression = new ValueQuery("foo.*.bar.*.uid = id");

        String expectedJson = "{\"field\":\"foo.*.bar.*.uid\",\"op\":\"=\",\"rvalue\":\"id\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);

    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidExpressionSpaceInFieldName() {
        new ValueQuery("fie ld1 = Red Hat Enterprise Linux");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidExpressionUnrecognizedOperator() {
        new ValueQuery("field1 ~= Red Hat Enterprise Linux");
    }
}

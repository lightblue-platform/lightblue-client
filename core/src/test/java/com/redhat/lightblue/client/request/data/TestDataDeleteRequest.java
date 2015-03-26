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
package com.redhat.lightblue.client.request.data;

import com.redhat.lightblue.client.expression.query.Query;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.skyscreamer.jsonassert.JSONAssert;

public class TestDataDeleteRequest extends AbstractLightblueRequestTest {

    private Query testQueryExpression = new Query() {
        public String toJson() {
            return "{\"field1\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };

	DataDeleteRequest request = new DataDeleteRequest();

	@Before
	public void setUp() throws Exception {
		request = new DataDeleteRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataDeleteRequest.PATH_PARAM_DELETE, request.getOperationPathParam());
	}

    @Test
    public void testRequestWithExpressionFormsProperBody() throws JSONException {
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + "}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

}

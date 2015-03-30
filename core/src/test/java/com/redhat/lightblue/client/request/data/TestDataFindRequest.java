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

import com.redhat.lightblue.client.enums.SortDirection;
import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.SortCondition;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.List;

public class TestDataFindRequest extends AbstractLightblueRequestTest  {

    private Query testQueryExpression = new Query() {
        public String toJson() {
            return "{\"field1\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };

    private Projection testProjection1 = new Projection () {
        public String toJson() {
            return "{\"field1\":\"name\"}";
        }
    };

    private Projection testProjection2 = new Projection() {
        public String toJson() {
            return "{\"field2\":\"address\"}";
        }
    };

    private SortCondition sortCondition1 = new SortCondition("field1", SortDirection.ASC);
    private SortCondition sortCondition2 = new SortCondition("field2", SortDirection.DESC);

	DataFindRequest request = new DataFindRequest();

	@Before
	public void setUp() throws Exception {
		request = new DataFindRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataFindRequest.PATH_PARAM_FIND, request.getOperationPathParam());
	}

    @Test
    public void testRequestWithExpressionAndSingleProjectionFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsArgumentsFormsProperBody() throws JSONException {
        request.select(testProjection1, testProjection2);
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsListFormsProperBody() throws JSONException {
        List<Projection> projections = new ArrayList<Projection>();
        projections.add(testProjection1);
        projections.add(testProjection2);

        request.select(projections);
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithExpressionProjectionAndSingleSortFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        List<SortCondition> sortConditions = new ArrayList<SortCondition>();
        sortConditions.add(sortCondition1);
        request.sort(sortConditions);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "]}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithSingleSortPassedAsArgumentFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "]}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testSetSortConditionsIsPassthroughForSort() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        List<SortCondition> sortConditions = new ArrayList<SortCondition>();
        sortConditions.add(sortCondition1);
        request.setSortConditions(sortConditions);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "]}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithExpressionProjectionAndMultiSortFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        List<SortCondition> sortConditions = new ArrayList<SortCondition>();
        sortConditions.add(sortCondition1);
        sortConditions.add(sortCondition2);
        request.sort(sortConditions);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "," + sortCondition2.toJson() + "]}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultiSortPassedAsParametersFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1, sortCondition2);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "," + sortCondition2.toJson() + "]}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

}

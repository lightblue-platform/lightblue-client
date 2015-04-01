package com.redhat.lightblue.client.request.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.redhat.lightblue.client.enums.SortDirection;
import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest.Operation;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.request.SortCondition;

public class TestDataFindRequest extends AbstractLightblueRequestTest {

    private final Query testQueryExpression = new Query() {
        @Override
        public String toJson() {
            return "{\"field1\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };

    private final Projection testProjection1 = new Projection() {
        @Override
        public String toJson() {
            return "{\"field1\":\"name\"}";
        }
    };

    private final Projection testProjection2 = new Projection() {
        @Override
        public String toJson() {
            return "{\"field2\":\"address\"}";
        }
    };

    private final SortCondition sortCondition1 = new SortCondition("field1", SortDirection.ASC);
    private final SortCondition sortCondition2 = new SortCondition("field2", SortDirection.DESC);

    DataFindRequest request = new DataFindRequest();

    @Before
    public void setUp() throws Exception {
        request = new DataFindRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals(Operation.FIND.getPathParam(), request.getOperationPathParam());
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

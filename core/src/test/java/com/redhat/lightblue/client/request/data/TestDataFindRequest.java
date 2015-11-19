package com.redhat.lightblue.client.request.data;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.Sort;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataFindRequest extends AbstractLightblueRequestTest {

    private final Query testQueryExpression = Query.withValue("test", Query.BinOp.neq, "hack");

    private final Projection testProjection1 = Projection.field("name", false, false);

    private final Projection testProjection2 = Projection.field("address", false, false);

    private final Sort sortCondition1 = Sort.asc("field1");
    private final Sort sortCondition2 = Sort.desc("field2");

    private DataFindRequest request;

    @Before
    public void setUp() throws Exception {
        request = new DataFindRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals("find", request.getOperationPathParam());
    }

    @Test
    public void testRequestWithExpressionAndSingleProjectionFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":" + testProjection1.toJson() + "}";

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
        request.select(testProjection1, testProjection2);
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithExpressionProjectionAndSingleSortFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"sort\":" + sortCondition1.toJson() + "}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithSingleSortPassedAsArgumentFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"sort\":" + sortCondition1.toJson() + "}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testSetSortConditionsIsPassthroughForSort() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"sort\":" + sortCondition1.toJson() + "}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithExpressionProjectionAndMultiSortFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1, sortCondition2);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"sort\":[" + sortCondition1.toJson() + "," + sortCondition2.toJson() + "]}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultiSortPassedAsParametersFormsProperBody() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1, sortCondition2);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"sort\":[" + sortCondition1.toJson() + "," + sortCondition2.toJson() + "]}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithRange() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1);
        request.range(0, 20);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"sort\":" + sortCondition1.toJson() + ",\"range\": [0,20]" + "}";
        JSONAssert.assertEquals(expected, request.getBody(), true);
    }

    @Test
    public void testRequestWithRangeNullTo() throws JSONException {
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1);
        request.range(0, null);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"sort\":" + sortCondition1.toJson() + ",\"range\": [0,null]" + "}";
        JSONAssert.assertEquals(expected, request.getBody(), true);
    }

}

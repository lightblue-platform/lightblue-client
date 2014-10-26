package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.enums.SortDirection;
import com.redhat.lightblue.client.query.QueryExpression;
import com.redhat.lightblue.client.projection.Projection;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by bmiller on 10/10/14.
 */
public class DataFindRequestTest {
    private static final String TEST_ENTITY_NAME = "testEntity";
    private static final String TEST_ENTITY_VERSION = "0.0.1";

    private QueryExpression testQueryExpression = new QueryExpression() {
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

    @Test
    public void testRequestHasDataFindRequestType() {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        assertEquals(RequestType.DATA_FIND, request.getRequestType());
    }

    @Test
    public void testRequestWithExpressionAndSingleProjectionFormsProperBody() throws JSONException {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1);
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsArgumentsFormsProperBody() throws JSONException {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1, testProjection2);
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsListFormsProperBody() throws JSONException {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
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
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
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
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "]}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testSetSortConditionsIsPassthroughForSort() throws JSONException {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
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
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
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
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1);
        request.where(testQueryExpression);
        request.sort(sortCondition1, sortCondition2);

        String expected = "{\"query\":" + testQueryExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "," + sortCondition2.toJson() + "]}";
        JSONAssert.assertEquals(expected, request.getBody(), false);
    }
}

package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.enums.SortDirection;
import com.redhat.lightblue.client.expression.Expression;
import com.redhat.lightblue.client.projection.Projection;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmiller on 10/10/14.
 */
public class DataFindRequestTest {
    private static final String TEST_ENTITY_NAME = "testEntity";
    private static final String TEST_ENTITY_VERSION = "0.0.1";

    private Expression testExpression = new Expression() {
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
            return "{\"field2\";\"address\"}";
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
    public void testRequestWithExpressionAndSingleProjectionFormsProperBody() {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1);
        request.where(testExpression);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "]}";

        assertEquals(expected, request.getBody());
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsArgumentsFormsProperBody() {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1, testProjection2);
        request.where(testExpression);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        assertEquals(expected, request.getBody());
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsListFormsProperBody() {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        List<Projection> projections = new ArrayList<Projection>();
        projections.add(testProjection1);
        projections.add(testProjection2);

        request.select(projections);
        request.where(testExpression);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        assertEquals(expected, request.getBody());
    }

    @Test
    public void testRequestWithExpressionProjectionAndSingleSortFormsProperBody() {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1);
        request.where(testExpression);
        List<SortCondition> sortConditions = new ArrayList<SortCondition>();
        sortConditions.add(sortCondition1);
        request.sort(sortConditions);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "]}";
        assertEquals(expected, request.getBody());
    }

    @Test
    public void testRequestWithSingleSortPassedAsArgumentFormsProperBody() {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1);
        request.where(testExpression);
        request.sort(sortCondition1);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "]}";
        assertEquals(expected, request.getBody());
    }

    @Test
    public void testSetSortConditionsIsPassthroughForSort() {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1);
        request.where(testExpression);
        List<SortCondition> sortConditions = new ArrayList<SortCondition>();
        sortConditions.add(sortCondition1);
        request.setSortConditions(sortConditions);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "]}";
        assertEquals(expected, request.getBody());
    }

    @Test
    public void testRequestWithExpressionProjectionAndMultiSortFormsProperBody() {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1);
        request.where(testExpression);
        List<SortCondition> sortConditions = new ArrayList<SortCondition>();
        sortConditions.add(sortCondition1);
        sortConditions.add(sortCondition2);
        request.sort(sortConditions);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "," + sortCondition2.toJson() + "]}";
        assertEquals(expected, request.getBody());
    }

    @Test
    public void testRequestWithMultiSortPassedAsParametersFormsProperBody() {
        DataFindRequest request = new DataFindRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.select(testProjection1);
        request.where(testExpression);
        request.sort(sortCondition1, sortCondition2);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":[" + testProjection1.toJson() + "],\"sort\":[" + sortCondition1.toJson() + "," + sortCondition2.toJson() + "]}";
        assertEquals(expected, request.getBody());
    }
}

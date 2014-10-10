package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.enums.SortDirection;
import com.redhat.lightblue.client.expression.Expression;
import com.redhat.lightblue.client.projection.Projection;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmiller on 10/10/14.
 */
public class DataFindRequestTest {
    private Expression testExpression = new Expression() {
        public String toJson() {
            return "{\"field\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };

    private Projection testProjection = new Projection () {
        public String toJson() {
            return "{\"field\":\"*\"}";
        }
    };

    private SortCondition sortCondition1 = new SortCondition("field1", SortDirection.ASC);
    private SortCondition sortCondition2 = new SortCondition("field2", SortDirection.DESC);

    @Test
    public void testRequestHasDataFindRequestType() {
        DataFindRequest request = new DataFindRequest();
        Assert.assertEquals(request.getRequestType(), RequestType.DATA_FIND);

    }

    @Test
    public void testRequestWithExpressionAndProjectionFormsProperBody() {
        DataFindRequest request = new DataFindRequest();
        request.select(testProjection);
        request.where(testExpression);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":" + testProjection.toJson() + "}";

        Assert.assertEquals(request.getBody(), expected);
    }

    @Test
    public void testRequestWithExpressionProjectionAndSingleSortFormsProperBody() {
        DataFindRequest request = new DataFindRequest();
        request.select(testProjection);
        request.where(testExpression);
        List<SortCondition> sortConditions = new ArrayList<SortCondition>();
        sortConditions.add(sortCondition1);
        request.sort(sortConditions);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":" + testProjection.toJson() + ",\"sort\":[" + sortCondition1.toJson() + "]}";
        Assert.assertEquals(request.getBody(), expected);
    }

    @Test
    public void testSetSortConditionsIsPassthroughForSort() {
        DataFindRequest request = new DataFindRequest();
        request.select(testProjection);
        request.where(testExpression);
        List<SortCondition> sortConditions = new ArrayList<SortCondition>();
        sortConditions.add(sortCondition1);
        request.setSortConditions(sortConditions);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":" + testProjection.toJson() + ",\"sort\":[" + sortCondition1.toJson() + "]}";
        Assert.assertEquals(request.getBody(), expected);
    }

    @Test
    public void testRequestWithExpressionProjectionAndMultiSortFormsProperBody() {
        DataFindRequest request = new DataFindRequest();
        request.select(testProjection);
        request.where(testExpression);
        List<SortCondition> sortConditions = new ArrayList<SortCondition>();
        sortConditions.add(sortCondition1);
        sortConditions.add(sortCondition2);
        request.sort(sortConditions);

        String expected = "{\"query\":" + testExpression.toJson() + ",\"projection\":" + testProjection.toJson() + ",\"sort\":[" + sortCondition1.toJson() + "," + sortCondition2.toJson() + "]}";
        Assert.assertEquals(request.getBody(), expected);
    }
}

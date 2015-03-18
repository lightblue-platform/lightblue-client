package com.redhat.lightblue.client.http;

import com.redhat.lightblue.client.http.model.SimpleModelObject;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static com.redhat.lightblue.client.expression.query.ValueQuery.withValue;
import static com.redhat.lightblue.client.projection.FieldProjection.includeField;

public class LightblueHttpClientTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    LightblueHttpClientMock client = new LightblueHttpClientMock();

    @Test
    public void testPojoMapping() throws IOException {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        client.setLightblueResponse("{\"matchCount\": 1, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash\", \"field\":\"value\"}], \"status\": \"COMPLETE\"}]");

        SimpleModelObject[] results = client.data(findRequest,
                SimpleModelObject[].class);

        Assert.assertEquals(1, results.length);

        Assert.assertTrue(new SimpleModelObject("idhash", "value").equals(results[0]));
    }

    @Test
    public void testPojoMappingWithErrorInResponse() throws IOException {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Error in lightblue response");

        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        String response = "{\"context\": \"rest/FindCommand/esbMessage\", \"errorCode\": \"rest-crud:RestFindError\", \"msg\": \"java.lang.IllegalArgumentException: Cannot call method public static com.redhat.lightblue.crud.FindRequest com.redhat.lightblue.crud.FindRequest.fromJson(com.fasterxml.jackson.databind.node.ObjectNode)\", \"objectType\": \"error\"}";

        client.setLightblueResponse(response);

        client.data(findRequest, SimpleModelObject[].class);
    }

    /**
     * This is bad json data in order to test a scenario.
     */
    @Test
    public void testPojoMappingWithParsingError() throws IOException {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Error parsing lightblue response");

        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        String response = "{\"processed\": \"bad json\"}";

        client.setLightblueResponse(response);

        client.data(findRequest, SimpleModelObject[].class);
    }

    @Test
    public void testPojoMappingWithEmptyProcessedResults() throws IOException {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        client.setLightblueResponse("{\"matchCount\": 0, \"modifiedCount\": 0}");

        SimpleModelObject[] results = client.data(findRequest, SimpleModelObject[].class);

        Assert.assertNull(results);
    }

    @Test
    public void testPojoMappingWithEmptyProcessedResults_EmptyArrayNode() throws IOException {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        client.setLightblueResponse("{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":0,\"processed\":[]}");

        SimpleModelObject[] results = client.data(findRequest, SimpleModelObject[].class);

        Assert.assertEquals(0, results.length);
    }

    @Test
    public void testPojoMappingWithEmptyProcessedResults_ForSingleResult() throws IOException {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        client.setLightblueResponse("{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":0,\"processed\":[]}");

        SimpleModelObject result = client.data(findRequest, SimpleModelObject.class);

        Assert.assertNull(result);
    }

}
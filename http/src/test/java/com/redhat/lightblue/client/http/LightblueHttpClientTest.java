package com.redhat.lightblue.client.http;

import static com.redhat.lightblue.client.expression.query.ValueQuery.withValue;
import static com.redhat.lightblue.client.projection.FieldProjection.includeField;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.http.model.SimpleModelObject;
import com.redhat.lightblue.client.request.data.DataFindRequest;

public class LightblueHttpClientTest {

    LightblueHttpClientMock client = new LightblueHttpClientMock();

    @Test
    public void testPojoMapping() throws IOException {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        client.setLightblueResponse("{\"matchCount\": 1, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash\", \"field\":\"value\"}], \"status\": \"COMPLETE\"}");

        SimpleModelObject[] results = client.data(findRequest,
                SimpleModelObject[].class);

        Assert.assertEquals(1, results.length);

        Assert.assertTrue(new SimpleModelObject("idhash", "value")
                .equals(results[0]));
    }

    @Test(expected = LightblueHttpClientException.class)
    public void testPojoMappingWithParsingError() throws IOException {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        String response = "{\"processed\":\"<p>This is not json</p>\"}";

        client.setLightblueResponse(response);

        client.data(findRequest, SimpleModelObject[].class);
    }

}

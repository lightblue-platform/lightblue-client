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
        ;
        Assert.assertTrue(new SimpleModelObject("idhash", "value")
        .equals(results[0]));
    }

    @Test
    public void testPojoMappingWithParsingError() throws IOException {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        String response = "{\"context\": \"rest/FindCommand/esbMessage\", \"errorCode\": \"rest-crud:RestFindError\", \"msg\": \"java.lang.IllegalArgumentException: Cannot call method public static com.redhat.lightblue.crud.FindRequest com.redhat.lightblue.crud.FindRequest.fromJson(com.fasterxml.jackson.databind.node.ObjectNode)\", \"objectType\": \"error\"}";

        client.setLightblueResponse(response);

        try {
            client.data(findRequest, SimpleModelObject[].class);

            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error parsing lightblue response"));
        }

    }

}

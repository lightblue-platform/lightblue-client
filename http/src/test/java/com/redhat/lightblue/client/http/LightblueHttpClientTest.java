package com.redhat.lightblue.client.http;

import static com.redhat.lightblue.client.expression.query.ValueQuery.withValue;
import static com.redhat.lightblue.client.projection.FieldProjection.includeField;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.model.SimpleModelObject;
import com.redhat.lightblue.client.http.transport.HttpTransport;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.response.LightblueException;

public class LightblueHttpClientTest {

    LightblueClientConfiguration config = new LightblueClientConfiguration();
    HttpTransport httpTransport = mock(HttpTransport.class);
    LightblueHttpClient client = new LightblueHttpClient(config, httpTransport);

    @Test
    public void testPojoMapping() throws Exception {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        String response = "{\"matchCount\": 1, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash\", \"field\":\"value\"}], \"status\": \"COMPLETE\"}";

        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString()))
                .thenReturn(response);

        SimpleModelObject[] results = client.data(findRequest,
                SimpleModelObject[].class);

        Assert.assertEquals(1, results.length);

        Assert.assertTrue(new SimpleModelObject("idhash", "value")
                .equals(results[0]));
    }

    @Test(expected = LightblueHttpClientException.class)
    public void testPojoMappingWithParsingError() throws Exception {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        String response = "{\"processed\":\"<p>This is not json</p>\"}";

        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString()))
                .thenReturn(response);

        client.data(findRequest, SimpleModelObject[].class);
    }

}

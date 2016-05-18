package com.redhat.lightblue.client.http;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.http.model.SimpleModelObject;
import com.redhat.lightblue.client.http.transport.HttpTransport;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.GenerateRequest;
import com.redhat.lightblue.client.response.DefaultLightblueDataResponse;
import com.redhat.lightblue.client.response.LightblueParseException;
import com.redhat.lightblue.client.util.JSON;

public class LightblueHttpClientTest {

    LightblueClientConfiguration config = new LightblueClientConfiguration();
    HttpTransport httpTransport = mock(HttpTransport.class);
    LightblueHttpClient client = new LightblueHttpClient(config, httpTransport);

    @Test
    public void testPojoMapping() throws Exception {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(Query.withValue("foo = bar"));
        findRequest.select(Projection.includeField("_id"));

        String response = "{\"matchCount\": 1, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash\", \"field\":\"value\"}], \"status\": \"COMPLETE\"}";

        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(response);

        SimpleModelObject[] results = client.data(findRequest, SimpleModelObject[].class);

        Assert.assertEquals(1, results.length);

        Assert.assertTrue(new SimpleModelObject("idhash", "value").equals(results[0]));
    }

    @Test
    public void testPrimitiveMapping() throws Exception {
        GenerateRequest request = new GenerateRequest("foo", "bar");
        request.path("x").nValues(3);
        String response = "{ \"processed\": [\"1\",\"2\",\"3\"], \"status\": \"COMPLETE\"}";
        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(response);

        String[] results = client.data(request, String[].class);

        Assert.assertEquals(3, results.length);
        Assert.assertEquals("1", results[0]);
        Assert.assertEquals("2", results[1]);
        Assert.assertEquals("3", results[2]);
    }

    @Test(expected = LightblueParseException.class)
    public void testPojoMappingWithParsingError() throws Exception {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(Query.withValue("foo = bar"));
        findRequest.select(Projection.includeField("_id"));

        String response = "{\"processed\":\"<p>This is not json</p>\"}";

        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(response);

        client.data(findRequest, SimpleModelObject[].class);
    }

    @Test(expected = LightblueException.class)
    public void testExceptionWhenLightblueIsDown() throws LightblueException {
        LightblueClientConfiguration c = new LightblueClientConfiguration();
        c.setUseCertAuth(false);
        c.setDataServiceURI("http://foo/bar");

        LightblueClient httpClient = new LightblueHttpClient(c);
        DataFindRequest r = new DataFindRequest("e", "v");
        r.where(Query.withValue("a = b"));
        r.select(Projection.includeField("foo"));

        httpClient.data(r);
        Assert.fail();

    }

    @Test(expected = LightblueParseException.class)
    public void testParseInvalidJson() throws Exception {
        DefaultLightblueDataResponse r = new DefaultLightblueDataResponse("invalid json", JSON.getDefaultObjectMapper());

        r.parseProcessed(SimpleModelObject.class);
    }

}

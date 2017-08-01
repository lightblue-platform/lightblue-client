package com.redhat.lightblue.client.http;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.io.StringBufferInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.MongoExecution;
import com.redhat.lightblue.client.MongoExecution.ReadPreference;
import com.redhat.lightblue.client.MongoExecution.WriteConcern;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.ResultStream;
import com.redhat.lightblue.client.http.model.SimpleModelObject;
import com.redhat.lightblue.client.http.transport.HttpTransport;
import com.redhat.lightblue.client.http.transport.HttpResponse;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.GenerateRequest;
import com.redhat.lightblue.client.response.DefaultLightblueDataResponse;
import com.redhat.lightblue.client.response.DiagnosticsElement;
import com.redhat.lightblue.client.response.LightblueDiagnosticsResponse;
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

        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(new FakeResponse(response, null));

        SimpleModelObject[] results = client.data(findRequest, SimpleModelObject[].class);

        Assert.assertEquals(1, results.length);

        Assert.assertTrue(new SimpleModelObject("idhash", "value").equals(results[0]));
    }

    @Test
    public void testPrimitiveMapping() throws Exception {
        GenerateRequest request = new GenerateRequest("foo", "bar");
        request.path("x").nValues(3);
        String response = "{ \"processed\": [\"1\",\"2\",\"3\"], \"status\": \"COMPLETE\"}";
        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(new FakeResponse(response, null));

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

        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(new FakeResponse(response, null));

        client.data(findRequest, SimpleModelObject[].class);
    }

	@Test(expected = LightblueException.class)
	public void testExceptionWhenLightblueIsDown() throws Exception {
		LightblueClientConfiguration c = new LightblueClientConfiguration();
		c.setUseCertAuth(false);
		c.setDataServiceURI("http://foo/bar");

		try (LightblueHttpClient httpClient = new LightblueHttpClient(c)) {
            DataFindRequest r = new DataFindRequest("e", "v");
            r.where(Query.withValue("a = b"));
            r.select(Projection.includeField("foo"));

            httpClient.data(r);
    		Assert.fail();
		}
	}

    @Test(expected = LightblueParseException.class)
    public void testParseInvalidJson() throws Exception {
        DefaultLightblueDataResponse r = new DefaultLightblueDataResponse("invalid json", null, JSON.getDefaultObjectMapper());

        r.parseProcessed(SimpleModelObject.class);
    }

    @Test
    public void testUsingDefaultExecution_ReadPreference() throws Exception {
        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(new FakeResponse("{}", null));

        LightblueClientConfiguration c = new LightblueClientConfiguration();
        c.setReadPreference(ReadPreference.primary);

        try (LightblueHttpClient httpClient = new LightblueHttpClient(c, httpTransport)) {
            DataFindRequest findRequest = new DataFindRequest("someEntity");
            findRequest.where(Query.withValue("a = b"));
            findRequest.select(Projection.includeField("foo"));

            httpClient.data(findRequest);

            String body = findRequest.getBody();
            Assert.assertTrue(body.contains(
                    "\"execution\":{\"readPreference\":\"primary\"}"));
        }
    }

    @Test
    public void testUsingDefaultExecution_WriteConcern() throws Exception {
        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(new FakeResponse("{}", null));

        LightblueClientConfiguration c = new LightblueClientConfiguration();
        c.setWriteConcern(WriteConcern.majority);

        try (LightblueHttpClient httpClient = new LightblueHttpClient(c, httpTransport)) {
            DataFindRequest findRequest = new DataFindRequest("someEntity");
            findRequest.where(Query.withValue("a = b"));
            findRequest.select(Projection.includeField("foo"));

            httpClient.data(findRequest);

            String body = findRequest.getBody();
            Assert.assertTrue(body.contains(
                    "\"execution\":{\"writeConcern\":\"majority\"}"));
        }
    }

    @Test
    public void testUsingDefaultExecution_MaxQueryTimeMS() throws Exception {
        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(new FakeResponse("{}", null));

        LightblueClientConfiguration c = new LightblueClientConfiguration();
        c.setMaxQueryTimeMS(1000);

        try (LightblueHttpClient httpClient = new LightblueHttpClient(c, httpTransport)) {
            DataFindRequest findRequest = new DataFindRequest("someEntity");
            findRequest.where(Query.withValue("a = b"));
            findRequest.select(Projection.includeField("foo"));

            httpClient.data(findRequest);

            String body = findRequest.getBody();
            Assert.assertTrue(body.contains(
                    "\"execution\":{\"maxQueryTimeMS\":1000}"));
        }
    }

    /**
     * If Execution is explicitly set on the request, then that should override the values in lightblue-client.properties
     */
    @Test
    public void testUsingDefaultExecution_OverrideExecution() throws Exception {
        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(new FakeResponse("{}", null));

        LightblueClientConfiguration c = new LightblueClientConfiguration();
        c.setReadPreference(ReadPreference.primary);

        try (LightblueHttpClient httpClient = new LightblueHttpClient(c, httpTransport)) {
            DataFindRequest findRequest = new DataFindRequest("someEntity");
            findRequest.where(Query.withValue("a = b"));
            findRequest.select(Projection.includeField("foo"));
            findRequest.execution(MongoExecution.withReadPreference(ReadPreference.nearest));

            httpClient.data(findRequest);

            String body = findRequest.getBody();
            Assert.assertTrue(body.contains(
                    "\"execution\":{\"readPreference\":\"nearest\"}"));
        }
    }


    private static final String streamingResponse=
        "{'entity':'test','status':'COMPLETE'}"+
        "{'processed':{'field':'value1'},'resultMetadata':{'documentVersion':'123:1231'}}"+
        "{'processed':{'field':'value2'},'resultMetadata':{'documentVersion':'123:1232'}}"+
        "{'processed':{'field':'value3'},'resultMetadata':{'documentVersion':'123:1233'}}"+
        "{'processed':{'field':'value4'},'resultMetadata':{'documentVersion':'123:1234'}}"+
        "{'processed':{'field':'value5'},'resultMetadata':{'documentVersion':'123:1235'}}"+
        "{'processed':{'field':'value6'},'resultMetadata':{'documentVersion':'123:1236'}}"+
        "{'processed':{'field':'value7'},'resultMetadata':{'documentVersion':'123:1237'}}"+
        "{'processed':{'field':'value8'},'resultMetadata':{'documentVersion':'123:1238'}}"+
        "{'processed':{'field':'value9'},'resultMetadata':{'documentVersion':'123:1239'},'last':true}";
    private static final String nonStreamingResponse=
        "{'entity':'test','status':'COMPLETE',"+
        "'processed':["+
        "{'field':'value1'},"+
        "{'field':'value2'},"+
        "{'field':'value3'},"+
        "{'field':'value4'},"+
        "{'field':'value5'},"+
        "{'field':'value6'},"+
        "{'field':'value7'},"+
        "{'field':'value8'},"+
        "{'field':'value9'}],"+
        "'resultMetadata':["+
        "{'documentVersion':'123:1231'},"+
        "{'documentVersion':'123:1232'},"+
        "{'documentVersion':'123:1233'},"+
        "{'documentVersion':'123:1234'},"+
        "{'documentVersion':'123:1235'},"+
        "{'documentVersion':'123:1236'},"+
        "{'documentVersion':'123:1237'},"+
        "{'documentVersion':'123:1238'},"+
        "{'documentVersion':'123:1239'}]}";
    
    @Test
    public void testStreaming() throws Exception {
        when(httpTransport.executeRequestGetStream(any(LightblueRequest.class),anyString())).
            thenReturn(new StringBufferInputStream(streamingResponse.replaceAll("'","\"")));
        LightblueClientConfiguration c = new LightblueClientConfiguration();
        try (LightblueHttpClient httpClient = new LightblueHttpClient(c, httpTransport)) {
            DataFindRequest findRequest = new DataFindRequest("test");
            findRequest.where(Query.withValue("a = b"));
            findRequest.select(Projection.includeField("foo"));
            ResultStream response=httpClient.prepareFind(findRequest);
            final List<JsonNode> docs=new ArrayList<JsonNode>();
            response.run(new ResultStream.ForEachDoc() {
                    @Override
                    public boolean processDocument(ResultStream.StreamDoc doc) {
                        docs.add(doc.doc);
                        return true;
                    }
                });
            Assert.assertEquals(9,docs.size());
            
        }
    }
    @Test
    public void testNotStreaming() throws Exception {
        when(httpTransport.executeRequestGetStream(any(LightblueRequest.class),anyString())).
            thenReturn(new StringBufferInputStream(nonStreamingResponse.replaceAll("'","\"")));
        LightblueClientConfiguration c = new LightblueClientConfiguration();
        try (LightblueHttpClient httpClient = new LightblueHttpClient(c, httpTransport)) {
            DataFindRequest findRequest = new DataFindRequest("test");
            findRequest.where(Query.withValue("a = b"));
            findRequest.select(Projection.includeField("foo"));
            ResultStream response=httpClient.prepareFind(findRequest);
            final List<JsonNode> docs=new ArrayList<JsonNode>();
            response.run(new ResultStream.ForEachDoc() {
                    @Override
                    public boolean processDocument(ResultStream.StreamDoc doc) {
                        docs.add(doc.doc);
                        return true;
                    }
                });
            Assert.assertEquals(9,docs.size());
            
        }
    }
    
    private static final String diagnosticsResponse = 
            "{\"MongoCRUDController\":"
            + "{\"healthy\":true,\"message\":\""
            + "[Mongo Config [lightbluemongo1.dev.a1.vary.redhat.com:27017, DatabaseName: metadata]=>ping:OK, "
            + "Mongo Config [lightbluemongo1.dev.a1.vary.redhat.com:27017, DatabaseName: data]=>ping:OK]\"},"
            + "\"ldap-auth-healthcheck\":{\"healthy\":true,"
            + "\"message\":\"LDAPConnection [DN: uid=lightblueapp,ou=serviceusers,ou=lightblue,dc=redhat,dc=com, "
            + "Status: true]\"}}";
   
    @Test
    public void testLightblueDiagnosticsElementPresent() throws Exception {
        
        DiagnosticsElement expectedElementDiagnostics = new DiagnosticsElement("MongoCRUDController", true, "[Mongo Config [lightbluemongo1.dev.a1.vary.redhat.com:27017, "
                + "DatabaseName: metadata]=>ping:OK, Mongo Config [lightbluemongo1.dev.a1.vary.redhat.com:27017, DatabaseName: data]=>ping:OK]");
        
        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(new FakeResponse(diagnosticsResponse, null));
        
        LightblueClientConfiguration c = new LightblueClientConfiguration();
        try (LightblueHttpClient httpClient = new LightblueHttpClient(c, httpTransport)) {
            
            LightblueDiagnosticsResponse response = httpClient.diagnostics();            
            DiagnosticsElement elementDiagnostics = response.getDiagnostics("MongoCRUDController");
            System.out.println(elementDiagnostics);
            
            Assert.assertEquals(expectedElementDiagnostics, elementDiagnostics);
        }
    }
    
    @Test
    public void testLightblueDiagnosticsElementNotPresent() throws Exception {        
        when(httpTransport.executeRequest(any(LightblueRequest.class), anyString())).thenReturn(new FakeResponse(diagnosticsResponse, null));
        
        LightblueClientConfiguration c = new LightblueClientConfiguration();
        try (LightblueHttpClient httpClient = new LightblueHttpClient(c, httpTransport)) {
            
            LightblueDiagnosticsResponse response = httpClient.diagnostics();            
            DiagnosticsElement elementDiagnostics = response.getDiagnostics("mongocrudcontroller");
            
            Assert.assertEquals(null, elementDiagnostics);
        }
    }

    private static class FakeResponse extends HttpResponse {

        protected FakeResponse(String body, Map<String, List<String>> headers) {
            super(body, headers);
        }

    }

}

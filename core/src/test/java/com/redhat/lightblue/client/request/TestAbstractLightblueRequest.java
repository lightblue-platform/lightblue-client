package com.redhat.lightblue.client.request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.util.JSON;

public class TestAbstractLightblueRequest extends AbstractLightblueRequestTest {

    private static class MockAbstractLightblueRequest extends AbstractLightblueRequest {

        private JsonNode body;

        public MockAbstractLightblueRequest(String entityName) {
            super(entityName);
        }

        public MockAbstractLightblueRequest(String entityName, String entityVersion) {
            super(entityName, entityVersion);
        }

        @Override
        public String getRestURI(String baseServiceURI) {
            return "/rest/data/find/"+updatedEntityName+"/"+updatedEntityVersion;
        }

        public void setBody(String body) {
            this.body = JSON.toJsonNode(body);
        }

        @Override
        public String getBody() {
            return getBodyJson().toString();
        }

        @Override
        public JsonNode getBodyJson() {
            return body;
        }

        @Override
        public HttpMethod getHttpMethod() {
            return HttpMethod.PUT;
        }
    }

    private MockAbstractLightblueRequest testRequest;

    private static final String updatedEntityName = "updatedEntity";
    private static final String updatedEntityVersion = "3.2.1";
    private static final String updatedBody = "{\"value\":\"name\"}";
    private static final String baseURI = "http://lightblue.io";

    @Before
    public void setUp() throws Exception {
        testRequest = new MockAbstractLightblueRequest(entityName, entityVersion);
        testRequest.setBody(body);
    }

    @Test
    public void testGetEntityName() {
        Assert.assertEquals(entityName, testRequest.getEntityName());
    }

    @Test
    public void testGetEntityVersion() {
        Assert.assertEquals(entityVersion, testRequest.getEntityVersion());
    }

    @Test
    public void testGetBody() {
        Assert.assertEquals(body, testRequest.getBody());
    }

    @Test
    public void testSetBody() {
        testRequest.setBody(updatedBody);
        Assert.assertEquals(updatedBody, testRequest.getBody());
    }

    @Test
    public void testAppendToURI() {
        StringBuilder initialURI = new StringBuilder();
        initialURI.append(baseURI);
        AbstractLightblueRequest.appendToURI(initialURI, "rest");
        Assert.assertEquals(baseURI + "/rest", initialURI.toString());
    }

    @Test
    public void testGetHttpMethod() {
        Assert.assertEquals(HttpMethod.PUT, testRequest.getHttpMethod());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("PUT /rest/data/find/updatedEntity/3.2.1, body: {\"name\":\"value\"}", testRequest.toString());
    }
}

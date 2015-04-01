package com.redhat.lightblue.client.request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractLightblueRequest extends AbstractLightblueRequestTest {

    private static class MockAbstractLightblueRequest extends AbstractLightblueRequest {
        private String body;

        @Override
        public String getRestURI(String baseServiceURI) {
            return null;
        }

        @Override
        public String getOperationPathParam() {
            return null;
        }

        public void setBody(String body) {
            this.body = body;
        }

        @Override
        public String getBody() {
            return body;
        }
    }

    MockAbstractLightblueRequest testRequest = new MockAbstractLightblueRequest();

    private static final String updatedEntityName = "updatedEntity";
    private static final String updatedEntityVersion = "3.2.1";
    private static final String updatedBody = "{\"value\":\"name\"}";
    private static final String baseURI = "http://lightblue.io";
    private static final String restURI = "http://lightblue.io/rest";

    @Before
    public void setUp() throws Exception {
        testRequest.setEntityName(entityName);
        testRequest.setEntityVersion(entityVersion);
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
    public void testSetEntityName() {
        testRequest.setEntityName(updatedEntityName);
        Assert.assertEquals(updatedEntityName, testRequest.getEntityName());
    }

    @Test
    public void testSetEntityVersion() {
        testRequest.setEntityVersion(updatedEntityVersion);
        Assert.assertEquals(updatedEntityVersion, testRequest.getEntityVersion());
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
        testRequest.appendToURI(initialURI, "rest");
        Assert.assertEquals(restURI, initialURI.toString());
    }

}

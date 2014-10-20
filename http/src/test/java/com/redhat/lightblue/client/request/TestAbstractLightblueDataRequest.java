package com.redhat.lightblue.client.request;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractLightblueDataRequest {

		
	AbstractLightblueDataRequest testRequest = new AbstractLightblueDataRequest() {

		@Override
    public HttpRequestBase getRestRequest(String baseServiceURI) {
	    return null;
    }

		@Override
    public String getOperationPathParam() {
	    return testOperation;
    }
	};
	
	private static final String initialEntityName = "lightblueEntity";
	private static final String initialEntityVersion = "1.2.3";
	private static final String baseURI = "http://lightblue.io/rest/";
	private static String testOperation = "dosomething";
	private static final String finalURI = baseURI + testOperation + "/" + initialEntityName + "/" + initialEntityVersion;
	
	@Before
	public void setUp() throws Exception {
		testRequest.setEntityName(initialEntityName);
		testRequest.setEntityVersion(initialEntityVersion);
	}

	@Test
	public void testGetRestURI() {
		Assert.assertEquals(finalURI, testRequest.getRestURI(baseURI));
	}

}

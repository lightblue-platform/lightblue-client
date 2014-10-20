package com.redhat.lightblue.client.request;

import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;

public class TestDataFindRequest {

	DataFindRequest request;
	private static final String restURI = "http://lightblue.io/rest";
	
	@Before
	public void setUp() throws Exception {
		request = new DataFindRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpGet deleteRequest = new HttpGet(restURI);
		HttpGet testDelete = request.getHttpGet(restURI);
		TestAbstractLightblueRequest.compareHttpRequestBase(deleteRequest, testDelete);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataDeleteRequest.PATH_PARAM_FIND, request.getOperationPathParam());
	}

}

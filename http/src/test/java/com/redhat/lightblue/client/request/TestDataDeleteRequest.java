package com.redhat.lightblue.client.request;

import org.apache.http.client.methods.HttpDelete;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.data.DataDeleteRequest;

public class TestDataDeleteRequest {

	DataDeleteRequest request;
	private static final String restURI = "http://lightblue.io/rest";
	
	@Before
	public void setUp() throws Exception {
		request = new DataDeleteRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpDelete deleteRequest = new HttpDelete(restURI);
		HttpDelete testDelete = request.getHttpDelete(restURI);
		TestAbstractLightblueRequest.compareHttpRequestBase(deleteRequest, testDelete);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataDeleteRequest.PATH_PARAM_DELETE, request.getOperationPathParam());
	}

}

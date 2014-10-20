package com.redhat.lightblue.client.request;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataSaveRequest;

public class TestDataSaveRequest {

	DataSaveRequest request;
	private static final String restURI = "http://lightblue.io/rest";
	private static final String body = "{\"name\":\"value\"}";
	
	@Before
	public void setUp() throws Exception {
		request = new DataSaveRequest();
	}

	@Test
	public void testGetRestRequest() throws IOException {
		HttpPost postRequest = new HttpPost(restURI);
		postRequest.setEntity(new StringEntity(body));
		HttpPost testDelete = request.getHttpPost(restURI, body);
		TestAbstractLightblueRequest.compareHttpPost(postRequest, testDelete);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataDeleteRequest.PATH_PARAM_SAVE, request.getOperationPathParam());
	}

}

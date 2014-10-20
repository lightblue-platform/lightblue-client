package com.redhat.lightblue.client.request;

import java.io.IOException;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;

public class TestDataInsertRequest {

	DataInsertRequest request;
	private static final String restURI = "http://lightblue.io/rest";
	private static final String body = "{\"name\":\"value\"}";
	
	@Before
	public void setUp() throws Exception {
		request = new DataInsertRequest();
	}

	@Test
	public void testGetRestRequest() throws IOException {
		HttpPut putRequest = new HttpPut(restURI);
		putRequest.setEntity(new StringEntity(body));
		HttpPut testDelete = request.getHttpPut(restURI, body);
		TestAbstractLightblueRequest.compareHttpPut(putRequest, testDelete);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataDeleteRequest.PATH_PARAM_INSERT, request.getOperationPathParam());
	}

}

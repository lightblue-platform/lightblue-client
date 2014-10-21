package com.redhat.lightblue.client.request.data;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.request.data.DataSaveRequest;

public class TestDataSaveRequest extends AbstractLightblueRequestTest {

	DataSaveRequest request;

	@Before
	public void setUp() throws Exception {
		request = new DataSaveRequest();
	}

	@Test
	public void testGetRestRequest() throws IOException {
		HttpPost expectedRequest = new HttpPost(baseURI + DataSaveRequest.PATH_PARAM_SAVE);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)request.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithEntityName() throws IOException {
		request = new DataSaveRequest(entityName, null);
		HttpPost expectedRequest = new HttpPost(baseURI + DataSaveRequest.PATH_PARAM_SAVE + "/" + entityName);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)request.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityNameAndVersion() throws IOException {
		request = new DataSaveRequest(entityName, entityVersion);
		HttpPost expectedRequest = new HttpPost(baseURI + DataSaveRequest.PATH_PARAM_SAVE + "/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)request.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataSaveRequest.PATH_PARAM_SAVE, request.getOperationPathParam());
	}

}

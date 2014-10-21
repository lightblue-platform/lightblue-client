package com.redhat.lightblue.client.request.data;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class TestDataUpdateRequest extends AbstractLightblueRequestTest {

	DataUpdateRequest request;
	
	@Before
	public void setUp() throws Exception {
		request = new DataUpdateRequest();
	}

	@Test
	public void testGetRestRequest() throws IOException {
		HttpPost expectedRequest = new HttpPost(baseURI + DataUpdateRequest.PATH_PARAM_UPDATE);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)request.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityName() throws IOException {
		request = new DataUpdateRequest(entityName, null);
		HttpPost expectedRequest = new HttpPost(baseURI + DataUpdateRequest.PATH_PARAM_UPDATE + "/" + entityName);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)request.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithEntityNameAndVersion() throws IOException {
		request = new DataUpdateRequest(entityName, entityVersion);
		HttpPost expectedRequest = new HttpPost(baseURI + DataUpdateRequest.PATH_PARAM_UPDATE + "/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)request.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataUpdateRequest.PATH_PARAM_UPDATE, request.getOperationPathParam());
	}

}

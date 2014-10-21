package com.redhat.lightblue.client.request.data;

import java.io.IOException;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;

public class TestDataInsertRequest extends AbstractLightblueRequestTest {

	DataInsertRequest request;
	
	@Before
	public void setUp() throws Exception {
		request = new DataInsertRequest();
	}

	@Test
	public void testGetRestRequest() throws IOException {
		HttpPut expectedRequest = new HttpPut(baseURI);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPut actualRequest = (HttpPut)request.getRestRequest(baseURI);
		compareHttpPut(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithEntityName() throws IOException {
		request = new DataInsertRequest(entityName, null);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName );
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPut actualRequest = (HttpPut)request.getRestRequest(baseURI);
		compareHttpPut(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithEntityNameAndVersion() throws IOException {
		request = new DataInsertRequest(entityName, entityVersion);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPut actualRequest = (HttpPut)request.getRestRequest(baseURI);
		compareHttpPut(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataInsertRequest.PATH_PARAM_INSERT, request.getOperationPathParam());
	}

}

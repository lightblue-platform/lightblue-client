package com.redhat.lightblue.client.request.data;

import org.apache.http.client.methods.HttpDelete;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.request.data.DataDeleteRequest;

public class TestDataDeleteRequest extends AbstractLightblueRequestTest {

	DataDeleteRequest request;
	private static final String operationName = "delete";

	@Before
	public void setUp() throws Exception {
		request = new DataDeleteRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpDelete expectedRequest = new HttpDelete(baseURI + operationName);
		HttpDelete actualRequest = (HttpDelete) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityName() {
		request = new DataDeleteRequest(entityName, null);
		HttpDelete expectedRequest = new HttpDelete(baseURI + operationName + "/" + entityName);
		HttpDelete actualRequest = (HttpDelete) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityNameAndVersion() {
		request = new DataDeleteRequest(entityName, entityVersion);
		HttpDelete expectedRequest = new HttpDelete(baseURI + operationName + "/" + entityName + "/" + entityVersion);
		HttpDelete actualRequest = (HttpDelete) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataDeleteRequest.PATH_PARAM_DELETE, request.getOperationPathParam());
	}

}

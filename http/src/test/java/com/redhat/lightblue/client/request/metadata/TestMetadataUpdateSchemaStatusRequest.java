package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpPut;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataUpdateSchemaStatusRequest extends AbstractLightblueRequestTest {

	MetadataUpdateSchemaStatusRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataUpdateSchemaStatusRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpPut expectedRequest = new HttpPut(baseURI);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityName() {
		request = new MetadataUpdateSchemaStatusRequest(entityName, null);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityNameAndVersion() {
		request = new MetadataUpdateSchemaStatusRequest(entityName, entityVersion);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName + "/" + entityVersion);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataUpdateSchemaStatusRequest.PATH_PARAM_UPDATE_SCHEMA_STATUS, request.getOperationPathParam());
	}

}

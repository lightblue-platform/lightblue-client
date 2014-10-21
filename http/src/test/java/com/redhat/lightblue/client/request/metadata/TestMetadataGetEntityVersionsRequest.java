package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityVersionsRequest extends AbstractLightblueRequestTest {

	MetadataGetEntityVersionsRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityVersionsRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpGet expectedRequest = new HttpGet(baseURI);
		HttpGet actualRequest = (HttpGet) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityName() {
		request = new MetadataGetEntityVersionsRequest(entityName, null);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName);
		HttpGet actualRequest = (HttpGet) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityNameAndVersion() {
		request = new MetadataGetEntityVersionsRequest(entityName, entityVersion);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion);
		HttpGet actualRequest = (HttpGet) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityRolesRequest.PATH_PARAM_GET_ENTITY_VERSIONS, request.getOperationPathParam());
	}

}

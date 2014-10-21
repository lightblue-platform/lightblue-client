package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityRolesRequest extends AbstractLightblueRequestTest {

	MetadataGetEntityRolesRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityRolesRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpGet expectedRequest = new HttpGet(baseURI + MetadataGetEntityRolesRequest.PATH_PARAM_GET_ENTITY_ROLES);
		HttpGet actualRequest = (HttpGet) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityName() {
		request = new MetadataGetEntityRolesRequest(entityName, null);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + MetadataGetEntityRolesRequest.PATH_PARAM_GET_ENTITY_ROLES);
		HttpGet actualRequest = (HttpGet) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityNameAndVersion() {
		request = new MetadataGetEntityRolesRequest(entityName, entityVersion);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion + "/" + MetadataGetEntityRolesRequest.PATH_PARAM_GET_ENTITY_ROLES);
		HttpGet actualRequest = (HttpGet) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityRolesRequest.PATH_PARAM_GET_ENTITY_ROLES, request.getOperationPathParam());
	}

}

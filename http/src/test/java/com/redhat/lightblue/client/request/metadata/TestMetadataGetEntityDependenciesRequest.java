package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityDependenciesRequest;

public class TestMetadataGetEntityDependenciesRequest extends AbstractLightblueRequestTest  {

	MetadataGetEntityDependenciesRequest request;
	
	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityDependenciesRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpGet expectedRequest = new HttpGet(baseURI + MetadataGetEntityDependenciesRequest.PATH_PARAM_GET_ENTITY_DEPENDENCIES);
		HttpGet actualRequest = (HttpGet)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityName() {
		request = new MetadataGetEntityDependenciesRequest(entityName, null);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + MetadataGetEntityDependenciesRequest.PATH_PARAM_GET_ENTITY_DEPENDENCIES);
		HttpGet actualRequest = (HttpGet)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithEntityNameAndVersion() {
		request = new MetadataGetEntityDependenciesRequest(entityName, entityVersion);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion +"/" + MetadataGetEntityDependenciesRequest.PATH_PARAM_GET_ENTITY_DEPENDENCIES);
		HttpGet actualRequest = (HttpGet)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityDependenciesRequest.PATH_PARAM_GET_ENTITY_DEPENDENCIES, request.getOperationPathParam());
	}

}

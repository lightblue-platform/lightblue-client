package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityMetadataRequest;

public class TestMetadataGetEntityMetadataRequest extends AbstractLightblueRequestTest  {

	MetadataGetEntityMetadataRequest request;
	
	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityMetadataRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpGet expectedRequest = new HttpGet(baseURI);
		HttpGet actualRequest = (HttpGet)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithEntityName() {
		request = new MetadataGetEntityMetadataRequest(entityName, null);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName);
		HttpGet actualRequest = (HttpGet)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityNameAndVersion() {
		request = new MetadataGetEntityMetadataRequest(entityName, entityVersion);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion);
		HttpGet actualRequest = (HttpGet)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityMetadataRequest.PATH_PARAM_GET_ENTITY_METADATA, request.getOperationPathParam());
	}

}

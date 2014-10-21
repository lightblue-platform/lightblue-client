package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityNamesRequest;

public class TestMetadataGetEntityNamesRequest extends AbstractLightblueRequestTest  {

	MetadataGetEntityNamesRequest request;
	
	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityNamesRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpGet expectedRequest = new HttpGet(baseURI);
		HttpGet actualRequest = (HttpGet)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithEntityName() {
		request = new MetadataGetEntityNamesRequest(entityName, null);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName);
		HttpGet actualRequest = (HttpGet)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityNameAndVersion() {
		request = new MetadataGetEntityNamesRequest(entityName, entityVersion);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion);
		HttpGet actualRequest = (HttpGet)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityNamesRequest.PATH_PARAM_GET_ENTITY_NAMES, request.getOperationPathParam());
	}

}

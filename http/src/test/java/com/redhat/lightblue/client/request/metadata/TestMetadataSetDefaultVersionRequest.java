package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpPost;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataSetDefaultVersionRequest extends AbstractLightblueRequestTest {

	MetadataSetDefaultVersionRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataSetDefaultVersionRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpPost expectedRequest = new HttpPost(baseURI + MetadataSetDefaultVersionRequest.PATH_PARAM_SET_DEFAULT_VERSION);
		request.setBody(body);
		HttpPost actualRequest = (HttpPost) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityName() {
		request = new MetadataSetDefaultVersionRequest(entityName, null);
		HttpPost expectedRequest = new HttpPost(baseURI + entityName + "/" + MetadataSetDefaultVersionRequest.PATH_PARAM_SET_DEFAULT_VERSION);
		request.setBody(body);
		HttpPost actualRequest = (HttpPost) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityNameAndVersion() {
		request = new MetadataSetDefaultVersionRequest(entityName, entityVersion);
		HttpPost expectedRequest = new HttpPost(baseURI + entityName + "/" + entityVersion + "/" + MetadataSetDefaultVersionRequest.PATH_PARAM_SET_DEFAULT_VERSION);
		request.setBody(body);
		HttpPost actualRequest = (HttpPost) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataSetDefaultVersionRequest.PATH_PARAM_SET_DEFAULT_VERSION, request.getOperationPathParam());
	}

}

package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpDelete;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataRemoveEntityRequest extends AbstractLightblueRequestTest {

	MetadataRemoveEntityRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataRemoveEntityRequest();
	}

	@Test
	public void testGetRestRequest() {
		HttpDelete expectedRequest = new HttpDelete(baseURI);
		HttpDelete actualRequest = (HttpDelete) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityName() {
		request = new MetadataRemoveEntityRequest(entityName, null);
		HttpDelete expectedRequest = new HttpDelete(baseURI + entityName);
		HttpDelete actualRequest = (HttpDelete) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithEntityNameAndVersion() {
		request = new MetadataRemoveEntityRequest(entityName, entityVersion);
		HttpDelete expectedRequest = new HttpDelete(baseURI + entityName + "/" + entityVersion);
		HttpDelete actualRequest = (HttpDelete) request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityDependenciesRequest.PATH_PARAM_REMOVE_ENTITY, request.getOperationPathParam());
	}

}

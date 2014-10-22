package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityDependenciesRequest extends AbstractLightblueRequestTest  {

	MetadataGetEntityDependenciesRequest request;
	
	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityDependenciesRequest();
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityDependenciesRequest.PATH_PARAM_GET_ENTITY_DEPENDENCIES, request.getOperationPathParam());
	}

}

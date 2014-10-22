package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataClearDefaultVersionsRequest extends AbstractLightblueRequestTest  {

	MetadataClearDefaultVersionRequest request;
	
	@Before
	public void setUp() throws Exception {
		request = new  MetadataClearDefaultVersionRequest();
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityDependenciesRequest.PATH_PARAM_CLEAR_DEFAULT_VERSION, request.getOperationPathParam());
	}

}

package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityNamesRequest extends AbstractLightblueRequestTest  {

	MetadataGetEntityNamesRequest request;
	
	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityNamesRequest();
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityNamesRequest.PATH_PARAM_GET_ENTITY_NAMES, request.getOperationPathParam());
	}

}

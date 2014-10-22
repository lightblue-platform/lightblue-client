package com.redhat.lightblue.client.request.metadata;

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
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityDependenciesRequest.PATH_PARAM_REMOVE_ENTITY, request.getOperationPathParam());
	}

}

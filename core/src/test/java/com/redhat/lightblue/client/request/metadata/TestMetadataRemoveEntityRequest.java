package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetadataRemoveEntityRequest extends AbstractLightblueRequestTest {

	MetadataRemoveEntityRequest request = new MetadataRemoveEntityRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataRemoveEntityRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityDependenciesRequest.PATH_PARAM_REMOVE_ENTITY, request.getOperationPathParam());
	}

}

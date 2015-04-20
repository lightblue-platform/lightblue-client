package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetadataGetEntityDependenciesRequest extends AbstractLightblueRequestTest {

	MetadataGetEntityDependenciesRequest request = new MetadataGetEntityDependenciesRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityDependenciesRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityDependenciesRequest.PATH_PARAM_GET_ENTITY_DEPENDENCIES, request.getOperationPathParam());
	}

}

package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityDependenciesRequest extends AbstractLightblueRequestTest {

	MetadataGetEntityDependenciesRequest request = new MetadataGetEntityDependenciesRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityDependenciesRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("dependencies", request.getOperationPathParam());
	}

}

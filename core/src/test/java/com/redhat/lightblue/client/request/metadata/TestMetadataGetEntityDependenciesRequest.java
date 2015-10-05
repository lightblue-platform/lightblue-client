package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityDependenciesRequest extends AbstractLightblueRequestTest {

	@Test
	public void testGetOperationPathParam() {
        MetadataGetEntityDependenciesRequest request = new MetadataGetEntityDependenciesRequest(entityName, entityVersion);

		Assert.assertEquals("dependencies", request.getOperationPathParam());
	}

}

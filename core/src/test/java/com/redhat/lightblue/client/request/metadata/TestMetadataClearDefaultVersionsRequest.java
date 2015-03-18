package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetadataClearDefaultVersionsRequest extends AbstractLightblueRequestTest {

	MetadataClearDefaultVersionRequest request = new MetadataClearDefaultVersionRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataClearDefaultVersionRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityDependenciesRequest.PATH_PARAM_CLEAR_DEFAULT_VERSION, request.getOperationPathParam());
	}

}

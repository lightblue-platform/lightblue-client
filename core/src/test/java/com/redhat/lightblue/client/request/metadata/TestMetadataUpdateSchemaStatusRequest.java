package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataUpdateSchemaStatusRequest extends AbstractLightblueRequestTest {

	MetadataUpdateSchemaStatusRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataUpdateSchemaStatusRequest();
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataUpdateSchemaStatusRequest.PATH_PARAM_UPDATE_SCHEMA_STATUS, request.getOperationPathParam());
	}

}

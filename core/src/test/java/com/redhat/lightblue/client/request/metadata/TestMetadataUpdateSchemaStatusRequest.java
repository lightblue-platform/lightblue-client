package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetadataUpdateSchemaStatusRequest extends AbstractLightblueRequestTest {

	MetadataUpdateSchemaStatusRequest request = new MetadataUpdateSchemaStatusRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataUpdateSchemaStatusRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataUpdateSchemaStatusRequest.PATH_PARAM_UPDATE_SCHEMA_STATUS, request.getOperationPathParam());
	}

}

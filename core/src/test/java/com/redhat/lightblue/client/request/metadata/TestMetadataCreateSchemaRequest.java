package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetadataCreateSchemaRequest extends AbstractLightblueRequestTest {

	MetadataCreateSchemaRequest request  = new MetadataCreateSchemaRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataCreateSchemaRequest(entityName, entityVersion);
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataCreateSchemaRequest.PATH_PARAM_CREATE_METADATA, request.getOperationPathParam());
	}

}

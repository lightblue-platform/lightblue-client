package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataCreateSchemaRequest extends AbstractLightblueRequestTest {

	MetadataCreateSchemaRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataCreateSchemaRequest();
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataCreateSchemaRequest.PATH_PARAM_CREATE_METADATA, request.getOperationPathParam());
	}

}

package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataCreateSchemaRequest extends AbstractLightblueRequestTest {

	MetadataCreateSchemaRequest request  = new MetadataCreateSchemaRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataCreateSchemaRequest(entityName, entityVersion, metadataSchema);
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("", request.getOperationPathParam());
	}

}

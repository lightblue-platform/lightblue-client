package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataClearDefaultVersionsRequest extends AbstractLightblueRequestTest {

	MetadataClearDefaultVersionRequest request = new MetadataClearDefaultVersionRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataClearDefaultVersionRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("default", request.getOperationPathParam());
	}

}

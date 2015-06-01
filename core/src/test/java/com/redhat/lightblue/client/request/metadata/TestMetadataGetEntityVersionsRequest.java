package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityVersionsRequest extends AbstractLightblueRequestTest {

	MetadataGetEntityVersionsRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityVersionsRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("", request.getOperationPathParam());
	}

}

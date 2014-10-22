package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataCreateRequest extends AbstractLightblueRequestTest {

	MetadataCreateRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataCreateRequest();
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataCreateRequest.PATH_PARAM_CREATE_METADATA, request.getOperationPathParam());
	}

}

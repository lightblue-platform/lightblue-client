package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataSetDefaultVersionRequest extends AbstractLightblueRequestTest {

	MetadataSetDefaultVersionRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataSetDefaultVersionRequest();
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataSetDefaultVersionRequest.PATH_PARAM_SET_DEFAULT_VERSION, request.getOperationPathParam());
	}

}

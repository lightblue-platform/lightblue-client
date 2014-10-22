package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataUpdateEntityInfoRequest extends AbstractLightblueRequestTest {

	MetadataUpdateEntityInfoRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataUpdateEntityInfoRequest();
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataUpdateEntityInfoRequest.PATH_PARAM_UPDATE_ENTITY_INFO, request.getOperationPathParam());
	}

}

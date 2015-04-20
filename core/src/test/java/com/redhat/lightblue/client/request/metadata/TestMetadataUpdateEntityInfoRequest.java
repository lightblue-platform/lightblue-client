package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetadataUpdateEntityInfoRequest extends AbstractLightblueRequestTest {

	MetadataUpdateEntityInfoRequest request = new MetadataUpdateEntityInfoRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataUpdateEntityInfoRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataUpdateEntityInfoRequest.PATH_PARAM_UPDATE_ENTITY_INFO, request.getOperationPathParam());
	}

}

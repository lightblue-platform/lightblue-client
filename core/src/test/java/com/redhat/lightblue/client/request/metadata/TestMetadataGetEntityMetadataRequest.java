package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetadataGetEntityMetadataRequest extends AbstractLightblueRequestTest  {

	MetadataGetEntityMetadataRequest request = new MetadataGetEntityMetadataRequest();
	
	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityMetadataRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataGetEntityMetadataRequest.PATH_PARAM_GET_ENTITY_METADATA, request.getOperationPathParam());
	}

}

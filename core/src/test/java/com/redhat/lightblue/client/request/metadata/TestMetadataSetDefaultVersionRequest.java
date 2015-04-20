package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetadataSetDefaultVersionRequest extends AbstractLightblueRequestTest {

	MetadataSetDefaultVersionRequest request = new MetadataSetDefaultVersionRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataSetDefaultVersionRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(MetadataSetDefaultVersionRequest.PATH_PARAM_SET_DEFAULT_VERSION, request.getOperationPathParam());
	}

}

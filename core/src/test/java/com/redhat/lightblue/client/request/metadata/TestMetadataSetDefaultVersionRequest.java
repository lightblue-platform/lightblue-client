package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataSetDefaultVersionRequest extends AbstractLightblueRequestTest {

	@Test
	public void testGetOperationPathParam() {
        MetadataSetDefaultVersionRequest request = new MetadataSetDefaultVersionRequest(entityName, entityVersion);

		Assert.assertEquals("default", request.getOperationPathParam());
	}

}

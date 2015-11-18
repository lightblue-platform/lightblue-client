package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityVersionsRequest extends AbstractLightblueRequestTest {

	@Test
	public void testGetOperationPathParam() {
        MetadataGetEntityVersionsRequest request = new MetadataGetEntityVersionsRequest(entityName);

        Assert.assertEquals(null, request.getOperationPathParam());
	}

}

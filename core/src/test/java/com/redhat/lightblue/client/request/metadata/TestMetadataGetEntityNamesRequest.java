package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityNamesRequest extends AbstractLightblueRequestTest {

	@Test
	public void testGetOperationPathParam() {
        MetadataGetEntityNamesRequest request = new MetadataGetEntityNamesRequest();

        Assert.assertEquals(null, request.getOperationPathParam());
	}

}

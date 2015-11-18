package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataCreateSchemaRequest extends AbstractLightblueRequestTest {

	@Test
	public void testGetOperationPathParam() {
        MetadataCreateSchemaRequest request = new MetadataCreateSchemaRequest(entityName, entityVersion);

        Assert.assertEquals("schema=" + entityVersion, request.getOperationPathParam());
	}

}

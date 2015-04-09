package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest.MetadataOperation;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataUpdateSchemaStatusRequest extends AbstractLightblueRequestTest {

    MetadataUpdateSchemaStatusRequest request = new MetadataUpdateSchemaStatusRequest();

    @Before
    public void setUp() throws Exception {
        request = new MetadataUpdateSchemaStatusRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals(MetadataOperation.UPDATE_SCHEMA_STATUS.getPathParam(), request.getOperationPathParam());
    }

}

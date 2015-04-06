package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest.MetadataOperation;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataCreateSchemaRequest extends AbstractLightblueRequestTest {

    MetadataCreateSchemaRequest request = new MetadataCreateSchemaRequest();

    @Before
    public void setUp() throws Exception {
        request = new MetadataCreateSchemaRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals(MetadataOperation.CREATE_SCHEMA.getPathParam(), request.getOperationPathParam());
    }

}

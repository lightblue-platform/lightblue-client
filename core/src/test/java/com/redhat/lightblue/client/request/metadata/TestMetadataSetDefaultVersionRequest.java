package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest.MetadataOperation;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataSetDefaultVersionRequest extends AbstractLightblueRequestTest {

    MetadataSetDefaultVersionRequest request = new MetadataSetDefaultVersionRequest();

    @Before
    public void setUp() throws Exception {
        request = new MetadataSetDefaultVersionRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals(MetadataOperation.SET_DEFAULT_VERSION.getPathParam(), request.getOperationPathParam());
    }

}

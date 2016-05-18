package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityMetadataRequest extends AbstractLightblueRequestTest {

    @Test
    public void testGetOperationPathParam() {
        MetadataGetEntityMetadataRequest request = new MetadataGetEntityMetadataRequest(entityName, entityVersion);

        Assert.assertEquals(null, request.getOperationPathParam());
    }

}

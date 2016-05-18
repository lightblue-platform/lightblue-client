package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataClearDefaultVersionsRequest extends AbstractLightblueRequestTest {

    @Test
    public void testGetOperationPathParam() {
        MetadataClearDefaultVersionRequest request = new MetadataClearDefaultVersionRequest(entityName);

        Assert.assertEquals("default", request.getOperationPathParam());
    }

}

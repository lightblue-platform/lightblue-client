package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataRemoveEntityRequest extends AbstractLightblueRequestTest {

    @Test
    public void testGetOperationPathParam() {
        MetadataRemoveEntityRequest request = new MetadataRemoveEntityRequest(entityName);

        Assert.assertEquals("", request.getOperationPathParam());
    }

}

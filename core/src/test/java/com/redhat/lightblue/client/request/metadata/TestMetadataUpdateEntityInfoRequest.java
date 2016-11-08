package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataUpdateEntityInfoRequest extends AbstractLightblueRequestTest {

    @Test
    public void testGetOperationPathParam() {
        MetadataUpdateEntityInfoRequest request = new MetadataUpdateEntityInfoRequest(entityName);

        Assert.assertEquals(null, request.getOperationPathParam());
    }

}

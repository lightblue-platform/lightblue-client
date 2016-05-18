package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataCreateNewEntityRequest extends AbstractLightblueRequestTest {

    @Test
    public void testGetOperationPathParam() {
        MetadataCreateNewEntityRequest request = new MetadataCreateNewEntityRequest(entityName, entityVersion);

        Assert.assertEquals("", request.getOperationPathParam());
    }

}

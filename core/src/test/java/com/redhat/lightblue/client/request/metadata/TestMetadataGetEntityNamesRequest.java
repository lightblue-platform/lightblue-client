package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest.MetadataOperation;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityNamesRequest extends AbstractLightblueRequestTest {

    MetadataGetEntityNamesRequest request = new MetadataGetEntityNamesRequest();

    @Before
    public void setUp() throws Exception {
        request = new MetadataGetEntityNamesRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals(MetadataOperation.GET_ENTITY_NAMES.getPathParam(), request.getOperationPathParam());
    }

}

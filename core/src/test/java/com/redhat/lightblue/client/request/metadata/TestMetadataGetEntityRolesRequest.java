package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest.MetadataOperation;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityRolesRequest extends AbstractLightblueRequestTest {

    MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest();

    @Before
    public void setUp() throws Exception {
        request = new MetadataGetEntityRolesRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals(MetadataOperation.GET_ENTITY_ROLES.getPathParam(), request.getOperationPathParam());
    }

}

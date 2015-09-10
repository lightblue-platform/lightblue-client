package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.enums.MetadataStatus;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataUpdateSchemaStatusRequest extends AbstractLightblueRequestTest {

    MetadataUpdateSchemaStatusRequest request;
    MetadataUpdateSchemaStatusRequest requestWithComment;

    @Before
    public void setUp() throws Exception {
        request = new MetadataUpdateSchemaStatusRequest(entityName, entityVersion, MetadataStatus.ACTIVE);
        requestWithComment = new MetadataUpdateSchemaStatusRequest(entityName, entityVersion, MetadataStatus.ACTIVE, metadataComment);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals("active", request.getOperationPathParam());
    }
    

    @Test
    public void testGetRestURI() {
    	Assert.assertEquals(finalMetadataURIWithComment, requestWithComment.getRestURI(baseURI));
    }

}

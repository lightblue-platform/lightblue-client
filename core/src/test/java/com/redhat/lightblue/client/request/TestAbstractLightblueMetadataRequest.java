package com.redhat.lightblue.client.request;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;

public class TestAbstractLightblueMetadataRequest extends AbstractLightblueRequestTest {

    LightblueMetadataRequest testRequest = new LightblueMetadataRequest(null,metadataOperation,entityName,entityVersion) {

    };

    @Test
    public void testGetRestURI() {
        Assert.assertEquals(finalMetadataURI, testRequest.getRestURI(baseURI));
    }

}

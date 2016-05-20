package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.http.HttpMethod;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Assert;
import org.junit.Test;

public class TestAbstractLightblueDataRequest extends AbstractLightblueRequestTest {

    LightblueDataRequest testRequest = new LightblueDataRequest(null,dataOperation,entityName, entityVersion) {

    };

    @Test
    public void testGetRestURI() {
        Assert.assertEquals(finalDataURI, testRequest.getRestURI(baseURI));
    }

}

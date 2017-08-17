package com.redhat.lightblue.client.request;

import org.junit.Assert;
import org.junit.Test;

public class TestLightblueDiagnosticsRequest extends AbstractLightblueRequestTest {

    LightblueDiagnosticsRequest testRequest = new LightblueDiagnosticsRequest();

    @Test
    public void testGetRestURI() {
        Assert.assertEquals(finalDiagnosticsURI, testRequest.getRestURI(baseURI));
    }
    
    @Test
    public void testRequestBodyNull() {
        Assert.assertEquals(null, testRequest.getBodyJson());
    }

}

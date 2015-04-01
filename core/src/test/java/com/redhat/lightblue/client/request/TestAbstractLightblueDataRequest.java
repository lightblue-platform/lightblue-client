package com.redhat.lightblue.client.request;

import org.junit.Assert;
import org.junit.Test;

public class TestAbstractLightblueDataRequest extends AbstractLightblueRequestTest {

    AbstractLightblueDataRequest testRequest = new AbstractLightblueDataRequest(entityName, entityVersion) {

        @Override
        public String getOperationPathParam() {
            return dataOperation;
        }

        @Override
        public String getBody() {
            // TODO Auto-generated method stub
            return null;
        }
    };

    @Test
    public void testGetRestURI() {
        Assert.assertEquals(finalDataURI, testRequest.getRestURI(baseURI));
    }

}

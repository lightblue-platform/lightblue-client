package com.redhat.lightblue.client.request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractLightblueDataRequest extends AbstractLightblueRequestTest {

    AbstractLightblueDataRequest testRequest = new AbstractLightblueDataRequest() {

        @Override
        public Operation getOperation() {
            return dataOperation;
        }

        @Override
        public String getBody() {
            // TODO Auto-generated method stub
            return null;
        }

    };

    @Before
    public void setUp() throws Exception {
        testRequest.setEntityName(entityName);
        testRequest.setEntityVersion(entityVersion);
    }

    @Test
    public void testGetRestURI() {
        Assert.assertEquals(finalDataURI, testRequest.getRestURI(baseURI));
    }

}

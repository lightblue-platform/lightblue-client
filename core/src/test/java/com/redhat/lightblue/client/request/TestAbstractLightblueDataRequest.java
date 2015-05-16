package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.http.HttpMethod;

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
            return null;
        }

        @Override
        public HttpMethod getHttpMethod() {
            return null;
        }
    };

    @Test
    public void testGetRestURI() {
        Assert.assertEquals(finalDataURI, testRequest.getRestURI(baseURI));
    }

}

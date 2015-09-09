package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.http.HttpMethod;

import com.fasterxml.jackson.databind.JsonNode;

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

            public JsonNode getBodyJson() {
                return null;
            }

        @Override
        public HttpMethod getHttpMethod() {
            return null;
        }

        @Override
        public String getOperation() {
            return dataOperation;
        }
    };

    @Test
    public void testGetRestURI() {
        Assert.assertEquals(finalDataURI, testRequest.getRestURI(baseURI));
    }

}

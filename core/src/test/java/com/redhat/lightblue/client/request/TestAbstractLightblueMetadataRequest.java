package com.redhat.lightblue.client.request;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;

public class TestAbstractLightblueMetadataRequest extends AbstractLightblueRequestTest {

    AbstractLightblueMetadataRequest testRequest = new AbstractLightblueMetadataRequest(entityName) {

        @Override
        public String getEntityVersion() {
            return entityVersion;
        }

		@Override
		public HttpMethod getHttpMethod() {
			return null;
		}

		@Override
		public String getOperationPathParam() {
			return metadataOperation;
		}

        @Override
        public JsonNode getBodyJson() {
            return null;
        }
	};

	@Test
	public void testGetRestURI() {
		Assert.assertEquals(finalMetadataURI, testRequest.getRestURI(baseURI));
	}

}

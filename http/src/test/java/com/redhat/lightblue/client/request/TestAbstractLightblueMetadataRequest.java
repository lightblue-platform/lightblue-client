package com.redhat.lightblue.client.request;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractLightblueMetadataRequest extends AbstractLightblueRequestTest {

	AbstractLightblueMetadataRequest testRequest = new AbstractLightblueMetadataRequest() {

		@Override
		public HttpRequestBase getRestRequest(String baseServiceURI) {
			return null;
		}

		@Override
		public String getOperationPathParam() {
			return metadataOperation;
		}
	};

	@Before
	public void setUp() throws Exception {
		testRequest.setEntityName(entityName);
		testRequest.setEntityVersion(entityVersion);
	}

	@Test
	public void testGetRestURI() {
		Assert.assertEquals(finalMetadataURI, testRequest.getRestURI(baseURI));
	}

}

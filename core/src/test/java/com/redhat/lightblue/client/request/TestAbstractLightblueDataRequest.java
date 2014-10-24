package com.redhat.lightblue.client.request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractLightblueDataRequest extends AbstractLightblueRequestTest {

		
	AbstractLightblueDataRequest testRequest = new AbstractLightblueDataRequest() {

		@Override
    public String getOperationPathParam() {
	    return dataOperation;
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

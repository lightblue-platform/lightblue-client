package com.redhat.lightblue.client.request.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataFindRequest extends AbstractLightblueRequestTest  {

	DataFindRequest request;

	@Before
	public void setUp() throws Exception {
		request = new DataFindRequest();
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataFindRequest.PATH_PARAM_FIND, request.getOperationPathParam());
	}

}

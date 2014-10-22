package com.redhat.lightblue.client.request.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataDeleteRequest extends AbstractLightblueRequestTest {

	DataDeleteRequest request;

	@Before
	public void setUp() throws Exception {
		request = new DataDeleteRequest();
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataDeleteRequest.PATH_PARAM_DELETE, request.getOperationPathParam());
	}

}

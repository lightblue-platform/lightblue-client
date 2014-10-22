package com.redhat.lightblue.client.request.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataInsertRequest extends AbstractLightblueRequestTest {

	DataInsertRequest request;
	
	@Before
	public void setUp() throws Exception {
		request = new DataInsertRequest();
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataInsertRequest.PATH_PARAM_INSERT, request.getOperationPathParam());
	}

}

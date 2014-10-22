package com.redhat.lightblue.client.request.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataSaveRequest extends AbstractLightblueRequestTest {

	DataSaveRequest request;

	@Before
	public void setUp() throws Exception {
		request = new DataSaveRequest();
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataSaveRequest.PATH_PARAM_SAVE, request.getOperationPathParam());
	}

}

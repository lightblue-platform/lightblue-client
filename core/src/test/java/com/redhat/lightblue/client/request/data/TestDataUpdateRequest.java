package com.redhat.lightblue.client.request.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataUpdateRequest extends AbstractLightblueRequestTest {

	DataUpdateRequest request = new DataUpdateRequest();
	
	@Before
	public void setUp() throws Exception {
		request = new DataUpdateRequest(entityName, entityVersion);
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("update", request.getOperationPathParam());
	}

}

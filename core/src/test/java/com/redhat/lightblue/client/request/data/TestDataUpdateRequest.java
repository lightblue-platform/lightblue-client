package com.redhat.lightblue.client.request.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataUpdateRequest extends AbstractLightblueRequestTest {

    private DataUpdateRequest request;

	@Before
	public void setUp() throws Exception {
		request = new DataUpdateRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("update", request.getOperationPathParam());
	}

    @Test
    public void testGetOperation() {
        Assert.assertEquals(Operation.UPDATE, request.getOperation());
    }

    @Test
    public void testGetHttpMethod() {
        Assert.assertEquals(HttpMethod.POST, request.getHttpMethod());
    }

}

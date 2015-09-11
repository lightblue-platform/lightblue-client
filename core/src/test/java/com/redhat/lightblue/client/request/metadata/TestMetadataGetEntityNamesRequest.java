package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.enums.MetadataStatus;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestMetadataGetEntityNamesRequest extends AbstractLightblueRequestTest {

	MetadataGetEntityNamesRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MetadataGetEntityNamesRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("", request.getOperationPathParam());
	}
	
	@Test
	public void testDefineStatusOptionalParam() {
		request.setStatus(MetadataStatus.DISABLED);
		Assert.assertEquals("disabled", request.getStatus().toString());
	}

}

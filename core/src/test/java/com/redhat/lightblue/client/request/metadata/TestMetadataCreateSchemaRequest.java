package com.redhat.lightblue.client.request.metadata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.util.JSON;

public class TestMetadataCreateSchemaRequest extends AbstractLightblueRequestTest {

	MetadataCreateSchemaRequest request  = new MetadataCreateSchemaRequest();

	@Before
	public void setUp() throws Exception {
		request = new MetadataCreateSchemaRequest(entityName, entityVersion, metadataSchema);
	}
	
	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("", request.getOperationPathParam());
	}
	
	@Test
    public void testGetRestURI() {
    	Assert.assertEquals(metadataCreateSchemaRequestUri, request.getRestURI(baseURI));
    }
	
	@Test
    public void testSchemaStringAsRequestBody() {
    	Assert.assertEquals(metadataSchema, request.getBody());
    }
	
	@Test
    public void testSchemaJsonAsRequestBody() {
		request = new MetadataCreateSchemaRequest(entityName, entityVersion, metadataSchemaJson);
    	Assert.assertEquals(metadataSchemaJson, JSON.toJsonNode(request.getBody()));
    }

}

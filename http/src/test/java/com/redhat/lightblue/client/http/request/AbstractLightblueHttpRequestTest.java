package com.redhat.lightblue.client.http.request;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Assert;

public class AbstractLightblueHttpRequestTest {

	protected static final String entityName = "lightblueEntity";
	protected static final String entityVersion = "1.2.3";
	protected static final String body = "{\"name\":\"value\"}";
	protected static final String baseURI = "http://lightblue.io/rest/";
	protected static final String dataOperation = "dosomethingwithdata";
	protected static final String metadataOperation = "dosomethingwithmetadata";
	protected static final String finalDataURI = baseURI + dataOperation + "/" + entityName + "/" + entityVersion;
	protected static final String finalMetadataURI = baseURI + entityName + "/" + entityVersion + "/" + metadataOperation;

	public void compareHttpRequestBase(HttpRequestBase request1, HttpRequestBase request2) {
		Assert.assertEquals(request1.getMethod(), request2.getMethod());
		Assert.assertEquals(request1.getURI(), request2.getURI());
	}
	
	public void compareHttpPost(HttpPost request1, HttpPost request2) throws IOException {
		compareHttpRequestBase(request1, request2);
		Assert.assertTrue(IOUtils.contentEquals(request1.getEntity().getContent(), request2.getEntity().getContent()));
	}

	public void compareHttpPut(HttpPut request1, HttpPut request2) throws IOException {
		compareHttpRequestBase(request1, request2);
		Assert.assertTrue(IOUtils.contentEquals(request1.getEntity().getContent(), request2.getEntity().getContent()));
	}
	
}

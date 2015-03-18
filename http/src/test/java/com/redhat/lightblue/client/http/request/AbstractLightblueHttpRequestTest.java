package com.redhat.lightblue.client.http.request;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;

import java.io.IOException;

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
		Assert.assertEquals(EntityUtils.toString(request1.getEntity()), EntityUtils.toString(request2.getEntity()));
	}

	public void compareHttpPut(HttpPut request1, HttpPut request2) throws IOException {
		compareHttpRequestBase(request1, request2);
		Assert.assertEquals(EntityUtils.toString(request1.getEntity()), EntityUtils.toString(request2.getEntity()));
	}
	
}

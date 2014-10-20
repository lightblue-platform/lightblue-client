package com.redhat.lightblue.client.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractLightblueRequest {

	AbstractLightblueRequest testRequest = new AbstractLightblueRequest() {
		@Override
		public String getRestURI(String baseServiceURI) {
			return null;
		}

		@Override
		public HttpRequestBase getRestRequest(String baseServiceURI) {
			return null;
		}

		@Override
		public String getOperationPathParam() {
			return null;
		}
	};

	private static final String initialEntityName = "lightblueEntity";
	private static final String initialEntityVersion = "1.2.3";
	private static final String initialBody = "{\"name\":\"value\"}";
	private static final String updatedEntityName = "updatedEntity";
	private static final String updatedEntityVersion = "3.2.1";
	private static final String updatedBody = "{\"value\":\"name\"}";
	private static final String baseURI = "http://lightblue.io";
	private static final String restURI = "http://lightblue.io/rest";

	@Before
	public void setUp() throws Exception {
		testRequest.setEntityName(initialEntityName);
		testRequest.setEntityVersion(initialEntityVersion);
		testRequest.setBody(initialBody);
	}

	@Test
	public void testGetEntityName() {
		Assert.assertEquals(initialEntityName, testRequest.getEntityName());
	}

	@Test
	public void testGetEntityVersion() {
		Assert.assertEquals(initialEntityVersion, testRequest.getEntityVersion());
	}

	@Test
	public void testGetBody() {
		Assert.assertEquals(initialBody, testRequest.getBody());
	}

	@Test
	public void testSetEntityName() {
		testRequest.setEntityName(updatedEntityName);
		Assert.assertEquals(updatedEntityName, testRequest.getEntityName());
	}

	@Test
	public void testSetEntityVersion() {
		testRequest.setEntityVersion(updatedEntityVersion);
		Assert.assertEquals(updatedEntityVersion, testRequest.getEntityVersion());
	}

	@Test
	public void testSetBody() {
		testRequest.setBody(updatedBody);
		Assert.assertEquals(updatedBody, testRequest.getBody());
	}

	@Test
	public void testAppendToURI() {
		StringBuilder initialURI = new StringBuilder();
		initialURI.append(baseURI);
		testRequest.appendToURI(initialURI, "rest");
		Assert.assertEquals(restURI, initialURI.toString());
	}

	@Test
	public void testGetHttpPost() throws UnsupportedEncodingException, IOException {
		HttpPost testPost = new HttpPost(restURI);
		testPost.setEntity(new StringEntity(initialBody));

		compareHttpPost(testPost, testRequest.getHttpPost(restURI, initialBody));
	}

	@Test
	public void testGetHttpPut() throws UnsupportedEncodingException, IOException {
		HttpPut testPut = new HttpPut(restURI);
		testPut.setEntity(new StringEntity(initialBody));

		compareHttpPut(testPut, testRequest.getHttpPut(restURI, initialBody));
	}

	@Test
	public void testGetHttpDelete() {
		HttpDelete testDelete = new HttpDelete(restURI);
		compareHttpRequestBase(testDelete, testRequest.getHttpDelete(restURI));
	}

	@Test
	public void testGetHttpGet() {
		HttpGet testGet = new HttpGet(restURI);
		compareHttpRequestBase(testGet, testRequest.getHttpGet(restURI));
	}

	public static void compareHttpRequestBase(HttpRequestBase request1, HttpRequestBase request2) {
		Assert.assertEquals(request1.getMethod(), request2.getMethod());
		Assert.assertEquals(request1.getURI(), request2.getURI());
	}
	
	public static void compareHttpPost(HttpPost request1, HttpPost request2) throws IOException {
		compareHttpRequestBase(request1, request2);
		Assert.assertTrue(IOUtils.contentEquals(request1.getEntity().getContent(), request2.getEntity().getContent()));
	}

	public static void compareHttpPut(HttpPut request1, HttpPut request2) throws IOException {
		compareHttpRequestBase(request1, request2);
		Assert.assertTrue(IOUtils.contentEquals(request1.getEntity().getContent(), request2.getEntity().getContent()));
	}
	
}

package com.redhat.lightblue.client.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractLightblueRequest extends AbstractLightblueRequestTest {

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

	private static final String updatedEntityName = "updatedEntity";
	private static final String updatedEntityVersion = "3.2.1";
	private static final String updatedBody = "{\"value\":\"name\"}";
	private static final String baseURI = "http://lightblue.io";
	private static final String restURI = "http://lightblue.io/rest";

	@Before
	public void setUp() throws Exception {
		testRequest.setEntityName(entityName);
		testRequest.setEntityVersion(entityVersion);
		testRequest.setBody(body);
	}

	@Test
	public void testGetEntityName() {
		Assert.assertEquals(entityName, testRequest.getEntityName());
	}

	@Test
	public void testGetEntityVersion() {
		Assert.assertEquals(entityVersion, testRequest.getEntityVersion());
	}

	@Test
	public void testGetBody() {
		Assert.assertEquals(body, testRequest.getBody());
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
		testPost.setEntity(new StringEntity(body));

		compareHttpPost(testPost, testRequest.getHttpPost(restURI, body));
	}

	@Test
	public void testGetHttpPut() throws UnsupportedEncodingException, IOException {
		HttpPut testPut = new HttpPut(restURI);
		testPut.setEntity(new StringEntity(body));

		compareHttpPut(testPut, testRequest.getHttpPut(restURI, body));
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


	
}

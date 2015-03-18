package com.redhat.lightblue.client.http.request;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TestAbstractLightblueHttpRequest extends AbstractLightblueHttpRequestTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetHttpPost() throws UnsupportedEncodingException, IOException {
		HttpPost testPost = new HttpPost(baseURI);
		testPost.setEntity(new StringEntity(body));

		compareHttpPost(testPost, AbstractLightblueHttpRequest.getHttpPost(baseURI, body));
	}

	@Test
	public void testGetHttpPut() throws UnsupportedEncodingException, IOException {
		HttpPut testPut = new HttpPut(baseURI);
		testPut.setEntity(new StringEntity(body));

		compareHttpPut(testPut, AbstractLightblueHttpRequest.getHttpPut(baseURI, body));
	}

	@Test
	public void testGetHttpDelete() {
		HttpDelete testDelete = new HttpDelete(baseURI);
		compareHttpRequestBase(testDelete, AbstractLightblueHttpRequest.getHttpDelete(baseURI));
	}

	@Test
	public void testGetHttpGet() {
		HttpGet testGet = new HttpGet(baseURI);
		compareHttpRequestBase(testGet, AbstractLightblueHttpRequest.getHttpGet(baseURI));
	}


	
}

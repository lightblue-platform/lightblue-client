/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.http.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;

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

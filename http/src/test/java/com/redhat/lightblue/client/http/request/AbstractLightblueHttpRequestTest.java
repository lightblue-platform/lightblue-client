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

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
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
		Assert.assertEquals(EntityUtils.toString(request1.getEntity()), EntityUtils.toString(request2.getEntity()));
	}

	public void compareHttpPut(HttpPut request1, HttpPut request2) throws IOException {
		compareHttpRequestBase(request1, request2);
		Assert.assertEquals(EntityUtils.toString(request1.getEntity()), EntityUtils.toString(request2.getEntity()));
	}
	
}

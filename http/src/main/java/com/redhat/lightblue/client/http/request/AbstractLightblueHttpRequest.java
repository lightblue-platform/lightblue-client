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

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

public class AbstractLightblueHttpRequest {

	protected static HttpPost getHttpPost(String uri, String body) {
  	HttpPost httpPost = new HttpPost(uri);
		try {
	    httpPost.setEntity(new StringEntity(body));
    } catch (UnsupportedEncodingException e) {
	    throw new RuntimeException(e);
    }
		return httpPost;
  }
  
  protected static HttpPut getHttpPut(String uri, String body) {
  	HttpPut httpPut = new HttpPut(uri);
		try {
	    httpPut.setEntity(new StringEntity(body));
    } catch (UnsupportedEncodingException e) {
	    throw new RuntimeException(e);
    }
		return httpPut;
  }
  
  protected static HttpDelete getHttpDelete(String uri) {
		return new HttpDelete(uri);
  }
  
  protected static HttpGet getHttpGet(String uri) {
		return new HttpGet(uri);
  }
	
}

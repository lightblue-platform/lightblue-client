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

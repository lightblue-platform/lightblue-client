package com.redhat.lightblue.client.http.request;

import org.apache.http.client.methods.HttpRequestBase;

public interface LightblueHttpRequest {
	
	HttpRequestBase getRestRequest(String baseServiceURI);
	
}

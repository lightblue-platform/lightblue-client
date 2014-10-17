package com.redhat.lightblue.client.request;

import org.apache.http.client.methods.HttpRequestBase;

public interface LightblueRequest {
	
	String getEntityName();
	
	String getEntityVersion();
	
	String getBody();
	
	void setEntityName(String entityName);

	void setEntityVersion(String entityVersion);

	void setBody(String body);
	
	String getRestURI(String baseServiceURI);
	
	HttpRequestBase getRestRequest(String baseServiceURI);
	
	String getOperationPathParam();
	
}

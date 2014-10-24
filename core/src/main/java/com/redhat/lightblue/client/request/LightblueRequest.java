package com.redhat.lightblue.client.request;


public interface LightblueRequest {
	
	String getEntityName();
	
	String getEntityVersion();
	
	String getBody();
	
	void setEntityName(String entityName);

	void setEntityVersion(String entityVersion);

	void setBody(String body);
	
	String getRestURI(String baseServiceURI);
	
	String getOperationPathParam();
	
}

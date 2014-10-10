package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;

public interface LightblueRequest {
	
	public RequestType getRequestType();
	
	public String getEntityName();
	
	public String getEntityVersion();
	
	public String getBody();
	
	public void setEntityName(String entityName);

	public void setEntityVersion(String entityVersion);

	public void setBody(String body);
	
}

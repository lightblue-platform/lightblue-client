package com.redhat.lightblue.client;


public interface LightblueRequest {
	
	public String getEntityName();
	
	public String getEntityVersion();
	
	public String getBody();
	
	public void setEntityName(String entityName);

	public void setEntityVersion(String entityVersion);

	public void setBody(String body);
	
}

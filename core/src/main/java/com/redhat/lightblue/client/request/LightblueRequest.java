package com.redhat.lightblue.client.request;

import java.text.SimpleDateFormat;

public interface LightblueRequest {

    public static final SimpleDateFormat lightblueDateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss.sssZ");
	
	String getEntityName();
	
	String getEntityVersion();
	
	String getBody();
	
	void setEntityName(String entityName);

	void setEntityVersion(String entityVersion);

    void setBody(String body);
	
	String getRestURI(String baseServiceURI);
	
	String getOperationPathParam();
	
}

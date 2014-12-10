package com.redhat.lightblue.client.request;

public interface LightblueRequest {

	String getEntityName();

	String getEntityVersion();

	String getBody();

	String getRestURI(String baseServiceURI);

	String getOperationPathParam();

}

package com.redhat.lightblue.client.request.data;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

public class DataDeleteRequest extends AbstractLightblueDataRequest {

	public DataDeleteRequest() {

	}

	public DataDeleteRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}
	
	@Override
	public HttpRequestBase getRestRequest(String baseServiceURI) {
		return getHttpDelete(getRestURI(baseServiceURI));
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_DELETE;
  }

}

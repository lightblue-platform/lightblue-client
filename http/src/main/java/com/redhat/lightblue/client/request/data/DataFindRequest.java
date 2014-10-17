package com.redhat.lightblue.client.request.data;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

public class DataFindRequest extends AbstractLightblueDataRequest {

	public DataFindRequest() {

	}

	public DataFindRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}

	@Override
	public HttpRequestBase getRestRequest(String baseServiceURI) {
		return getHttpPost(getRestURI(baseServiceURI), this.getBody());
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_FIND;
  }

}

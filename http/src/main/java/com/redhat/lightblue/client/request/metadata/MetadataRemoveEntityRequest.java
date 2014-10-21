package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataRemoveEntityRequest extends AbstractLightblueMetadataRequest {

	public MetadataRemoveEntityRequest() {

	}

	public MetadataRemoveEntityRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}

	@Override
	public HttpRequestBase getRestRequest(String baseServiceURI) {
		return getHttpDelete(getRestURI(baseServiceURI));
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_REMOVE_ENTITY;
  }

}

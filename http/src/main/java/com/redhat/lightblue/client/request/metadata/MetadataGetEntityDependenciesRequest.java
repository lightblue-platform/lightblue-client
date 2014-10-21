package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataGetEntityDependenciesRequest extends AbstractLightblueMetadataRequest {

	public MetadataGetEntityDependenciesRequest() {

	}

	public MetadataGetEntityDependenciesRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}

	@Override
	public HttpRequestBase getRestRequest(String baseServiceURI) {
		return getHttpGet(getRestURI(baseServiceURI));
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_GET_ENTITY_DEPENDENCIES;
  }

}

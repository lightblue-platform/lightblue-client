package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataSetDefaultVersionRequest extends AbstractLightblueMetadataRequest {

	public MetadataSetDefaultVersionRequest() {

	}

	public MetadataSetDefaultVersionRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}

	@Override
	public HttpRequestBase getRestRequest(String baseServiceURI) {
		return getHttpPost(getRestURI(baseServiceURI), this.getBody());
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_SET_DEFAULT_VERSION;
  }

}

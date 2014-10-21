package com.redhat.lightblue.client.request.metadata;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataCreateSchemaRequest extends AbstractLightblueMetadataRequest {

	public MetadataCreateSchemaRequest() {

	}

	public MetadataCreateSchemaRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}

	@Override
	public HttpRequestBase getRestRequest(String baseServiceURI) {
		return getHttpPut(getRestURI(baseServiceURI), this.getBody());
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_CREATE_SCHEMA;
  }

}

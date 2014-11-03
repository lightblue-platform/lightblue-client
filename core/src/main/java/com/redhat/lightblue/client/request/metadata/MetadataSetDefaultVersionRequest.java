package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataSetDefaultVersionRequest extends AbstractLightblueMetadataRequest {

	public MetadataSetDefaultVersionRequest() {

	}

	public MetadataSetDefaultVersionRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_SET_DEFAULT_VERSION;
  }

}

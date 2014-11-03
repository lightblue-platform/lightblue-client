package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataUpdateEntityInfoRequest extends AbstractLightblueMetadataRequest {

	public MetadataUpdateEntityInfoRequest() {

	}

	public MetadataUpdateEntityInfoRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_UPDATE_ENTITY_INFO;
  }

}

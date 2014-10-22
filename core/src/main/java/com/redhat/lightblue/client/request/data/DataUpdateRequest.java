package com.redhat.lightblue.client.request.data;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

public class DataUpdateRequest extends AbstractLightblueDataRequest {

	public DataUpdateRequest() {

	}

	public DataUpdateRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_UPDATE;
  }

}

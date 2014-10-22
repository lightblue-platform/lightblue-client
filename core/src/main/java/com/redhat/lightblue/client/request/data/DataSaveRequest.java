package com.redhat.lightblue.client.request.data;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

public class DataSaveRequest extends AbstractLightblueDataRequest {

	public DataSaveRequest() {

	}

	public DataSaveRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
	}

	@Override
  public String getOperationPathParam() {
	  return PATH_PARAM_SAVE;
  }

}

package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;

public class DataInsertRequest extends AbstractLightblueRequest {

	@Override
	public RequestType getRequestType() {
		return RequestType.DATA_INSERT;
	}

}

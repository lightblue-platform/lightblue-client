package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;

public class DataSaveRequest extends AbstractLightblueRequest {

	@Override
	public RequestType getRequestType() {
		return RequestType.DATA_SAVE;
	}

    @Override
    public String getBody() {
        return null;
    }

}

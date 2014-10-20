package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;

public class DataUpdateRequest extends AbstractLightblueRequest {

	@Override
	public RequestType getRequestType() {
		return RequestType.DATA_UPDATE;
	}

    @Override
    public String getBody() {
        return null;
    }

}

package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;

public class DataDeleteRequest extends AbstractLightblueRequest {

	@Override
	public RequestType getRequestType() {
		return RequestType.DATA_DELETE;
	}

    @Override
    public String getBody() {
        return null;
    }

}

package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;

public class MetadataRequest extends AbstractLightblueRequest {

	@Override
	public RequestType getRequestType() {
		return RequestType.METADATA;
	}

    @Override
    public String getBody() {
        return null;
    }

}

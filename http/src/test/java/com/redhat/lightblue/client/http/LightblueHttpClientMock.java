package com.redhat.lightblue.client.http;

import org.apache.http.client.methods.HttpRequestBase;
import com.redhat.lightblue.client.LightblueClientConfiguration;

import com.redhat.lightblue.client.response.DefaultLightblueResponse;
import com.redhat.lightblue.client.response.LightblueResponse;

public class LightblueHttpClientMock extends LightblueHttpClient {

	private String lightblueResponse;

	public void setLightblueResponse(String lightblueResponse) {
		this.lightblueResponse = lightblueResponse;
	}

	public LightblueHttpClientMock() {
		super(new LightblueClientConfiguration());
	}

	@Override
	protected LightblueResponse callService(HttpRequestBase httpOperation) {
		return new DefaultLightblueResponse(lightblueResponse);
	}

}

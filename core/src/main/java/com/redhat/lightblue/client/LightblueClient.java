package com.redhat.lightblue.client;

import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

import java.io.IOException;

public interface LightblueClient {

	public abstract void setConfigFilePath(String configFilePath);

	public abstract String getConfigFilePath();

	public abstract LightblueResponse metadata(LightblueRequest lightblueRequest);

	public abstract LightblueResponse data(LightblueRequest lightblueRequest);

    public abstract <T> T data(LightblueRequest lightblueRequest, Class<T> type) throws IOException;

}

package com.redhat.lightblue.client;

import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;

public interface LightblueClient {

    SimpleDateFormat lightblueDateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss.sssZ");

	public abstract void setConfigFilePath(String configFilePath);

	public abstract String getConfigFilePath();

	public abstract LightblueResponse metadata(LightblueRequest lightblueRequest);

	public abstract LightblueResponse data(LightblueRequest lightblueRequest);

    public abstract <T> T data(LightblueRequest lightblueRequest, Class<T> type) throws IOException;

}

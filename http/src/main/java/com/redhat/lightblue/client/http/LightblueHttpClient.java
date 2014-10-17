package com.redhat.lightblue.client.http;

import org.apache.http.impl.client.CloseableHttpClient;

public interface LightblueHttpClient {

	public abstract CloseableHttpClient getClient();

}
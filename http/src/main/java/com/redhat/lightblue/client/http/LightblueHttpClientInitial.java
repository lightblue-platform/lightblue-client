package com.redhat.lightblue.client.http;

import org.apache.http.impl.client.CloseableHttpClient;

public interface LightblueHttpClientInitial {

	public abstract CloseableHttpClient getClient();

}
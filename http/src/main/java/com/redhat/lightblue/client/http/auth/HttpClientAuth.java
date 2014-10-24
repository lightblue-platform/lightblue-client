package com.redhat.lightblue.client.http.auth;

import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpClientAuth {

	public abstract CloseableHttpClient getClient();

}
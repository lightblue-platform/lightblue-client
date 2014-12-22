package com.redhat.lightblue.client.http.auth;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public interface HttpClientAuth {

	public abstract CloseableHttpClient getClient();

	/** Allows for a preconfigured client. Configuration may be overwritten. */
	public abstract CloseableHttpClient getClient(HttpClientBuilder builder);

}

package com.redhat.lightblue.client.http.auth;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @deprecated Use {@link com.redhat.lightblue.client.http.auth.ApacheHttpClients} instead, or
 * create your own client using {@link org.apache.http.impl.client.HttpClients}. If you need an
 * {@link org.apache.http.conn.ssl.SSLConnectionSocketFactory}, see
 * {@link com.redhat.lightblue.client.http.auth.SslSocketFactories}.
 */
@Deprecated
public interface HttpClientAuth {

	public abstract CloseableHttpClient getClient();
}

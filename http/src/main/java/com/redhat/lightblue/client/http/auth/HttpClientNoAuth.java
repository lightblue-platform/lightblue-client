package com.redhat.lightblue.client.http.auth;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

/**
 * @deprecated Use {@link com.redhat.lightblue.client.http.auth.ApacheHttpClients} instead, or
 * create your own client using {@link org.apache.http.impl.client.HttpClients}. If you need an
 * {@link org.apache.http.conn.ssl.SSLConnectionSocketFactory}, see
 * {@link com.redhat.lightblue.client.http.auth.SslSocketFactories}.
 */
@Deprecated
public class HttpClientNoAuth implements HttpClientAuth {
    private final SSLConnectionSocketFactory sslSocketFactory;

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientNoAuth.class);

    public HttpClientNoAuth() {
        try {
            sslSocketFactory = SslSocketFactories.defaultNoAuthSocketFactory();
        } catch (GeneralSecurityException e) {
            LOGGER.error("Error creating jks from certificates: ", e);
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.redhat.lightblue.client.http.LightblueHttpClientI#getClient()
     */
    @Override
    public CloseableHttpClient getClient() {
        return HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }
}

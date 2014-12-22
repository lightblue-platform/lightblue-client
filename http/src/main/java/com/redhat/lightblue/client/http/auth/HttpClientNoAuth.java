package com.redhat.lightblue.client.http.auth;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

public class HttpClientNoAuth implements HttpClientAuth {
    private final SSLConnectionSocketFactory sslSocketFactory;

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientNoAuth.class);

    public HttpClientNoAuth() {
        try {
            SSLContextBuilder sslCtxBuilder = new SSLContextBuilder();
            sslCtxBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslSocketFactory = new SSLConnectionSocketFactory(sslCtxBuilder.build());
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
        return getClient(HttpClients.custom());
    }

    @Override
    public CloseableHttpClient getClient(HttpClientBuilder builder) {
        return builder
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setSSLSocketFactory(sslSocketFactory)
                .build();
    }

    @Override
    public SSLConnectionSocketFactory getSSLConnectionSocketFactory() {
        return sslSocketFactory;
    }
}

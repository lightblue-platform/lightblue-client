package com.redhat.lightblue.client.http.auth;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
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
    private final Registry<ConnectionSocketFactory> socketFactoryRegistry;

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientNoAuth.class);

    public HttpClientNoAuth() {
        SSLConnectionSocketFactory sslSocketFactory;

        try {
            sslSocketFactory = SslSocketFactories.defaultNoAuthSocketFactory();
        } catch (GeneralSecurityException e) {
            LOGGER.error("Error creating jks from certificates: ", e);
            throw new RuntimeException(e);
        }

        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
    }

    /* (non-Javadoc)
     * @see com.redhat.lightblue.client.http.LightblueHttpClientI#getClient()
     */
    @Override
    public CloseableHttpClient getClient() {
        HttpClientConnectionManager connManager;
        connManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
        return ApacheHttpClients.forConnectionManager(connManager);
    }
}

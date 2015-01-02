package com.redhat.lightblue.client.http.servlet;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.LightblueHttpClient;
import com.redhat.lightblue.client.http.auth.HttpClientAuth;
import com.redhat.lightblue.client.http.auth.HttpClientCertAuth;
import com.redhat.lightblue.client.http.auth.HttpClientNoAuth;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example usage:
 * <pre><code>
 *     public class HttpClientProducer {
 *         {@literal@}Produces @ApplicationScoped
 *         public CloseableHttpClient getClient() {
 *             return LightblueHttpClientBuilder.create()
 *                     .withMaxConnections(200)
 *                     .withMaxConnectionsPerRoute(50)
 *                     .build();
 *
 *         }
 *     }
 * </code></pre>
 */
public class LightblueHttpClientBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightblueHttpClientBuilder.class);

    // Defaults
    private LightblueClientConfiguration lightblueClientConfig = new PropertiesLightblueClientConfiguration();
    private int maxConnections = 100;
    private int maxConnectionsPerRoute = 20;

    public static LightblueHttpClientBuilder create() {
        return new LightblueHttpClientBuilder();
    }

    public static CloseableHttpClient buildWithDefaults() {
        return create().build();
    }

    protected LightblueHttpClientBuilder() {}

    /**
     * @see org.apache.http.impl.conn.PoolingHttpClientConnectionManager
     * @see org.apache.http.pool.ConnPoolControl
     */
    public LightblueHttpClientBuilder withMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    /**
     * @see org.apache.http.impl.conn.PoolingHttpClientConnectionManager
     * @see org.apache.http.pool.ConnPoolControl
     */
    public LightblueHttpClientBuilder withMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
        return this;
    }

    /**
     * Client configuration is used to determine SSL connectivity properties and authn information
     * for talking to the lightblue service.
     */
    public LightblueHttpClientBuilder withLightblueClientConfiguration(
            LightblueClientConfiguration lightblueClientConfig) {
        this.lightblueClientConfig = lightblueClientConfig;
        return this;
    }

    public CloseableHttpClient build() {;
        return getLightblueHttpClient();
    }

    private CloseableHttpClient getLightblueHttpClient() {
        SSLConnectionSocketFactory sslSocketFactory = getSocketFactory();
        Registry<ConnectionSocketFactory> socketFactoryRegistry;
        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        connectionManager.setMaxTotal(maxConnections);

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                        // I don't think this is necessary?
                .setSSLSocketFactory(sslSocketFactory)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }

    /**
     * @return An SSL socket factory to use based on whether or not config.useCertAuth() is true.
     */
    private SSLConnectionSocketFactory getSocketFactory() {
        HttpClientAuth clientAuth;

        if(lightblueClientConfig.useCertAuth()) {
            LOGGER.debug("Using certificate authentication");
            clientAuth = new HttpClientCertAuth(lightblueClientConfig);
        } else {
            LOGGER.debug("Using no authentication");
            clientAuth = new HttpClientNoAuth();
        }

        return clientAuth.getSSLConnectionSocketFactory();
    }
}

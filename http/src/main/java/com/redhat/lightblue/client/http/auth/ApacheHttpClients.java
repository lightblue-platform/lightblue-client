package com.redhat.lightblue.client.http.auth;

import com.redhat.lightblue.client.LightblueClientConfiguration;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public abstract class ApacheHttpClients {
    /** Same as Apache's default in {@link org.apache.http.impl.client.HttpClientBuilder}. */
    private static final String DEFAULT_MAX_CONNECTIONS_PER_ROUTE = "5";

    /** Same as Apache's default in {@link org.apache.http.impl.client.HttpClientBuilder}. */
    private static final String DEFAULT_MAX_CONNECTIONS = "10";

    /**
     * Same as java.net's defaults for persistent connections. What it calls "maxConnections" is
     * really max connections per route in Apache HTTP client terms:
     *
     * <blockquote>Indicates the maximum number of connections per destination to be kept alive at any given time</blockquote>
     *
     * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html">HTTP Persistent Connections</a>
     */
    private static final String DEFAULT_MAX_CONNECTIONS_PER_ROUTE_SYSTEM_PROPERTY = "http.maxConnections";

    private static final String DEFAULT_MAX_CONNECTIONS_TOTAL_SYSTEM_PROPERTY = "http.maxConnectionsTotal";

    public static CloseableHttpClient fromLightblueClientConfiguration(LightblueClientConfiguration config)
            throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
            KeyStoreException, KeyManagementException, IOException {
        return fromLightblueClientConfiguration(config, defaultMaxConnections(),
                defaultMaxConnectionsPerRoute());
    }

    public static CloseableHttpClient fromLightblueClientConfiguration(LightblueClientConfiguration config,
            int maxConnections, int maxConnectionsPerRoute)
            throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
            KeyStoreException, KeyManagementException, IOException {
        return forSslSocketFactory(SslSocketFactories.fromLightblueClientConfig(config),
                maxConnections, maxConnectionsPerRoute);
    }

    public static CloseableHttpClient forSslSocketFactory(SSLConnectionSocketFactory sslSocketFactory) {
        return forSslSocketFactory(sslSocketFactory, defaultMaxConnections(),
                defaultMaxConnectionsPerRoute());
    }

    public static CloseableHttpClient forSslSocketFactory(SSLConnectionSocketFactory sslSocketFactory,
            int maxConnections, int maxConnectionsPerRoute) {
        Registry<ConnectionSocketFactory> socketFactoryRegistry;
        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        connManager.setMaxTotal(maxConnections);

        return HttpClients.custom()
                .setConnectionManager(connManager)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }

    private static int defaultMaxConnectionsPerRoute() {
        String maxConnections = System.getProperty(DEFAULT_MAX_CONNECTIONS_PER_ROUTE_SYSTEM_PROPERTY,
                DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
        return Integer.parseInt(maxConnections);
    }

    private static int defaultMaxConnections() {
        String maxConnections = System.getProperty(DEFAULT_MAX_CONNECTIONS_TOTAL_SYSTEM_PROPERTY,
                DEFAULT_MAX_CONNECTIONS);
        return Integer.parseInt(maxConnections);
    }
}

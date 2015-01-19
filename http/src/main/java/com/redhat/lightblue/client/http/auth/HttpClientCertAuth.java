package com.redhat.lightblue.client.http.auth;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

/**
 * @deprecated Use {@link com.redhat.lightblue.client.http.auth.ApacheHttpClients} instead, or
 * create your own client using {@link org.apache.http.impl.client.HttpClients}. If you need an
 * {@link org.apache.http.conn.ssl.SSLConnectionSocketFactory}, see
 * {@link com.redhat.lightblue.client.http.auth.SslSocketFactories}.
 */
@Deprecated
public class HttpClientCertAuth implements HttpClientAuth {
    private final Registry<ConnectionSocketFactory> socketFactoryRegistry;

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientCertAuth.class);

	public HttpClientCertAuth() {
		this(PropertiesLightblueClientConfiguration.fromDefault());
	}

	public HttpClientCertAuth(String configFilePath) {
		this(PropertiesLightblueClientConfiguration.fromPath(Paths.get(configFilePath)));
	}

	public HttpClientCertAuth(LightblueClientConfiguration configuration) {
		String caFilePath = configuration.getCaFilePath();
		String certFilePath = configuration.getCertFilePath();
		String certPassword = configuration.getCertPassword();
		String certAlias = configuration.getCertAlias();

        SSLConnectionSocketFactory sslSocketFactory;

		try {
			InputStream caFile = getClass().getClassLoader().getResourceAsStream(caFilePath);
			InputStream certFile = getClass().getClassLoader().getResourceAsStream(certFilePath);

			sslSocketFactory = SslSocketFactories.defaultCertAuthSocketFactory(caFile, certFile,
					certPassword.toCharArray(), certAlias);
		} catch (GeneralSecurityException | IOException e) {
			LOGGER.error("Error creating jks from certificates: ", e);
			throw new RuntimeException(e);
		}

        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redhat.lightblue.client.http.auth.HttpClientAuth#getClient()
	 */
	@Override
	public CloseableHttpClient getClient() {
        HttpClientConnectionManager connManager;
        connManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
		return ApacheHttpClients.forConnectionManager(connManager);
	}
}

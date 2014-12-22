package com.redhat.lightblue.client.http.auth;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;

import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class HttpClientCertAuth implements HttpClientAuth {

	private final String caFilePath;
	private final String certFilePath;
	private final String certPassword;
	private final String certAlias;

	private final Logger LOGGER = LoggerFactory.getLogger(HttpClientCertAuth.class);

	public HttpClientCertAuth() {
		this(new PropertiesLightblueClientConfiguration());
	}

	public HttpClientCertAuth(String configFilePath) {
		this(new PropertiesLightblueClientConfiguration(Paths.get(configFilePath)));
	}

	public HttpClientCertAuth(LightblueClientConfiguration configuration) {
		caFilePath = configuration.getCaFilePath();
		certFilePath = configuration.getCertFilePath();
		certPassword = configuration.getCertPassword();
		certAlias = configuration.getCertAlias();
	}

	private SSLContext getSSLContext() {
		SSLContext ctx = null;
		try {
			/* Load CA-Chain file */
			CertificateFactory cf = CertificateFactory.getInstance("X509");

			X509Certificate cert = (X509Certificate) cf.generateCertificate(getClass().getClassLoader().getResourceAsStream(caFilePath));

			KeyStore ks = KeyStore.getInstance("pkcs12");
			KeyStore jks = KeyStore.getInstance("jks");

			char[] ks_password = this.certPassword.toCharArray();

			ks.load(getClass().getClassLoader().getResourceAsStream(certFilePath), ks_password);

			jks.load(null, ks_password);

			jks.setCertificateEntry(this.certAlias, cert);
			Key key = ks.getKey(this.certAlias, ks_password);
			Certificate[] chain = ks.getCertificateChain(this.certAlias);
			jks.setKeyEntry("anykey", key, ks_password, chain);

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(jks);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, ks_password);

			ctx = SSLContext.getInstance("TLSv1");
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		} catch (GeneralSecurityException | IOException e) {
			LOGGER.error("Error creating jks from certificates: ", e);
			throw new RuntimeException(e);
		}
		return (ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redhat.lightblue.client.http.auth.HttpClientAuth#getClient()
	 */
	@Override
	public CloseableHttpClient getClient() {
		return getClient(HttpClients.custom());
	}

	@Override
	public CloseableHttpClient getClient(HttpClientBuilder builder) {
		SSLContext sslcontext = this.getSSLContext();

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		return builder
				.setSSLSocketFactory(sslsf)
				.setRedirectStrategy(new LaxRedirectStrategy())
				.build();
	}

}

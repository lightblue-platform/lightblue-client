package com.redhat.lightblue.client.http.auth;

import java.security.GeneralSecurityException;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientNoAuth implements HttpClientAuth {

	private final Logger LOGGER = LoggerFactory.getLogger(HttpClientNoAuth.class);
	
    /* (non-Javadoc)
		 * @see com.redhat.lightblue.client.http.LightblueHttpClientI#getClient()
		 */
    @Override
    public CloseableHttpClient getClient() {
    	return getClient(HttpClients.custom());
    }

    @Override
    public CloseableHttpClient getClient(HttpClientBuilder builder) {
        try {
            SSLContextBuilder sslCtxBuilder = new SSLContextBuilder();
            sslCtxBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslCtxBuilder.build());

            return builder
                    .setRedirectStrategy(new LaxRedirectStrategy())
                    .setSSLSocketFactory(sslsf)
                    .build();
        } catch (GeneralSecurityException e) {
            LOGGER.error("Error creating jks from certificates: ", e);
            throw new RuntimeException(e);
        }
    }
}

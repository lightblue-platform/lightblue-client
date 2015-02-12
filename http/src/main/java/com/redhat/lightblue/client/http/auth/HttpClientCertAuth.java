package com.redhat.lightblue.client.http.auth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;

/**
 * @deprecated Use {@link com.redhat.lightblue.client.http.auth.ApacheHttpClients} instead, or
 * create your own client using {@link org.apache.http.impl.client.HttpClients}. If you need an
 * {@link org.apache.http.conn.ssl.SSLConnectionSocketFactory}, see
 * {@link com.redhat.lightblue.client.http.auth.SslSocketFactories}.
 */
@Deprecated
public class HttpClientCertAuth implements HttpClientAuth {
    private final SSLConnectionSocketFactory sslSocketFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientCertAuth.class);
    private static final String[] TLS_V1 = new String[] { "TLSv1" };
    private static final String FILE_PROTOCOL = "file://";

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

        try {
            InputStream caFile = loadFile(caFilePath);
            InputStream certFile = loadFile(certFilePath);

            sslSocketFactory = SslSocketFactories.defaultCertAuthSocketFactory(caFile, certFile,
                    certPassword.toCharArray(), certAlias);
        } catch (GeneralSecurityException | IOException e) {
            LOGGER.error("Error creating jks from certificates: ", e);
            throw new RuntimeException(e);
        }
    }

    private InputStream loadFile(String filePath) throws FileNotFoundException{
        if(filePath.startsWith(FILE_PROTOCOL)){
            return new FileInputStream(filePath.substring(FILE_PROTOCOL.length()));
        }
        return getClass().getClassLoader().getResourceAsStream(filePath);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.redhat.lightblue.client.http.auth.HttpClientAuth#getClient()
     */
    @Override
    public CloseableHttpClient getClient() {
        return HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }
}

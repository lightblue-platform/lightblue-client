package com.redhat.lightblue.client.http.auth;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.lightblue.client.LightblueClientConfiguration;

public class SslSocketFactories {
    private static final Logger LOGGER = LoggerFactory.getLogger(SslSocketFactories.class);

    private static final String TLSV1 = "TLSv1";

    private static final String[] SUPPORTED_PROTOCOLS = new String[]{TLSV1};
    private static final String[] SUPPORTED_CIPHER_SUITES = null;

    /**
     * @return A default SSL socket factory based on whether or not the
     * specified
     * {@link com.redhat.lightblue.client.LightblueClientConfiguration} is
     * configured to use cert based authentication.
     */
    public static SSLConnectionSocketFactory fromLightblueClientConfig(LightblueClientConfiguration config)
            throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
            KeyStoreException, KeyManagementException, IOException {
        if (config.useCertAuth()) {
            validateLightblueClientConfigForCertAuth(config);

            return defaultCertAuthSocketFactory(
                    CertificateManager.getCaCertFiles(config.getCaFilePaths()),
                    CertificateManager.loadFile(config.getCertFilePath()),
                    config.getCertPassword().toCharArray(), config.getCertAlias(), config.isAcceptSelfSignedCert());
        }

        return defaultNoAuthSocketFactory();
    }

    /**
     * @return A default SSL socket factory that does not connect using an
     * authenticated Lightblue certificate, but instead trust's self signed SSL
     * certificates.
     */
    public static SSLConnectionSocketFactory defaultNoAuthSocketFactory()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder sslCtxBuilder = new SSLContextBuilder();
        sslCtxBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        return new SSLConnectionSocketFactory(sslCtxBuilder.build());
    }

    /**
     * @param certAuthorityFile The cert authority file, used to determine the
     * authenticity of the server's SSL certificate.
     * @param authCert The authenticating certificate, used by Lightblue to
     * authenticate.
     * @param authCertPassword The password for the authenticating certificate.
     * @param authCertAlias The alias for the authenticating certificate.
     * @return A default SSL socket factory that assumes a Lightblue instance
     * configured to use SSL certificate based authentication. It does not
     * accept self signed certs, and instead must be provided a certificate
     * authority file to communicate with the Lightblue server.
     */
    public static SSLConnectionSocketFactory defaultCertAuthSocketFactory(
            List<InputStream> certAuthorityFiles, InputStream authCert, char[] authCertPassword,
            String authCertAlias, boolean acceptSelfSignedCert)
            throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyManagementException {

        Set<Certificate> certificates = CertificateManager.getCertificates(certAuthorityFiles);
        KeyStore pkcs12KeyStore = CertificateManager.getPkcs12KeyStore(authCert, authCertPassword);
        KeyStore sunKeyStore = CertificateManager.getJksKeyStore(certificates, pkcs12KeyStore, authCertAlias, authCertPassword);
        SSLContext sslContext = getSSLContext(sunKeyStore, pkcs12KeyStore, authCertPassword, acceptSelfSignedCert);

        return new SSLConnectionSocketFactory(sslContext, SUPPORTED_PROTOCOLS, SUPPORTED_CIPHER_SUITES,
                NoopHostnameVerifier.INSTANCE);
    }

    public static SSLSocketFactory javaNetSslSocketFactory(LightblueClientConfiguration config)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
            UnrecoverableKeyException, KeyManagementException {
        validateLightblueClientConfigForCertAuth(config);

        return javaNetSslSocketFactory(
                CertificateManager.getCaCertFiles(config.getCaFilePaths()),
                CertificateManager.loadFile(config.getCertFilePath()),
                config.getCertPassword().toCharArray(), config.getCertAlias(), config.isAcceptSelfSignedCert());
    }

    public static SSLSocketFactory javaNetSslSocketFactory(List<InputStream> certAuthorityFiles, InputStream authCert,
                                                           char[] authCertPassword, String authCertAlias, boolean acceptSelfSignedCert)
            throws CertificateException, NoSuchAlgorithmException,
            KeyStoreException, IOException, UnrecoverableKeyException, KeyManagementException {

        Objects.requireNonNull(certAuthorityFiles, "Could not load the CA cert file.  Please verify that "
                + "the certificate file is on the classpath or defined on the file system using the 'file://'"
                + "prefix.");
        Objects.requireNonNull(authCert, "Could not load the auth cert file.  Please verify that "
                + "the certificate file is on the classpath or defined on the file system using the "
                + "'file://' prefix.");

        Set<Certificate> caCertificates = CertificateManager.getCertificates(certAuthorityFiles);

        KeyStore pkcs12KeyStore = CertificateManager.getPkcs12KeyStore(authCert, authCertPassword);
        KeyStore sunKeyStore = CertificateManager.getJksKeyStore(caCertificates, pkcs12KeyStore, authCertAlias, authCertPassword);
        SSLContext sslContext = getSSLContext(sunKeyStore, pkcs12KeyStore, authCertPassword, acceptSelfSignedCert);
        return sslContext.getSocketFactory();
    }

    /**
     * Naive trust manager trusts all.
     *
     */
    private static TrustManager createNaiveTrustManager() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }

    private static SSLContext getSSLContext(KeyStore trustKeyStore, KeyStore authKeyStore,
                                                   char[] authCertPassword, boolean acceptSelfSignedCert)
            throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException,
            KeyManagementException {

        TrustManager[] trustManagers = null;

        if (acceptSelfSignedCert) {
            LOGGER.warn("Accepting self-signed certs. This is very insecure - use only in dev environments!");
            trustManagers = new TrustManager[] { createNaiveTrustManager() };
        } else {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustKeyStore);
            trustManagers = tmf.getTrustManagers();
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(authKeyStore, authCertPassword);

        SSLContext ctx = SSLContext.getInstance(TLSV1);
        ctx.init(kmf.getKeyManagers(), trustManagers, null);

        return ctx;
    }

    private static void validateLightblueClientConfigForCertAuth(LightblueClientConfiguration config) {
        if (config.getCaFilePaths().isEmpty()) {
            throw new IllegalArgumentException("Must provide a caFilePath.");
        }
        if (StringUtils.isBlank(config.getCertFilePath())) {
            throw new IllegalArgumentException("Must provide a certFilePath.");
        }
        if (StringUtils.isBlank(config.getCertPassword())) {
            throw new IllegalArgumentException("Must provide a certPassword.");
        }

        // certAlias is not required if only one cert exists in the keystore (usually the case)
    }
}

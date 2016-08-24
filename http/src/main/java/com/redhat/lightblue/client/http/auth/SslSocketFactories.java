package com.redhat.lightblue.client.http.auth;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SslSocketFactories {
    private static final Logger LOGGER = LoggerFactory.getLogger(SslSocketFactories.class);

    private static final String TLSV1 = "TLSv1";

    private static final String[] SUPPORTED_PROTOCOLS = new String[]{TLSV1};
    private static final String[] SUPPORTED_CIPHER_SUITES = null;
    private static final String FILE_PROTOCOL = "file://";

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
                return defaultCertAuthSocketFactory(getCaCertFiles(config), loadFile(config.getCertFilePath()),
                        config.getCertPassword().toCharArray(), config.getCertAlias());
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
            String authCertAlias)
            throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyManagementException {

        Set<Certificate> certificates = getCertificates(certAuthorityFiles);
        KeyStore pkcs12KeyStore = getPkcs12KeyStore(authCert, authCertPassword);
        KeyStore sunKeyStore = getJksKeyStore(certificates, pkcs12KeyStore, authCertAlias, authCertPassword);
        SSLContext sslContext = getDefaultSSLContext(sunKeyStore, pkcs12KeyStore, authCertPassword);

        return new SSLConnectionSocketFactory(sslContext, SUPPORTED_PROTOCOLS, SUPPORTED_CIPHER_SUITES,
                NoopHostnameVerifier.INSTANCE);
    }

    public static SSLSocketFactory javaNetSslSocketFactory(LightblueClientConfiguration config)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
            UnrecoverableKeyException, KeyManagementException {
        validateLightblueClientConfigForCertAuth(config);

        return javaNetSslSocketFactory(getCaCertFiles(config), loadFile(config.getCertFilePath()),
                config.getCertPassword().toCharArray(), config.getCertAlias());
    }

    private static List<InputStream> getCaCertFiles(LightblueClientConfiguration config) throws FileNotFoundException {
        List<String> caCertFilePaths = config.getCaFilePaths();
        List<InputStream> caCertFiles = new ArrayList<>();

        for(String caCertFilePath : caCertFilePaths) {
            caCertFiles.add(loadFile(caCertFilePath));
        }

        return caCertFiles;
    }

    public static SSLSocketFactory javaNetSslSocketFactory(List<InputStream> certAuthorityFiles, InputStream authCert,
                                                           char[] authCertPassword, String authCertAlias)
            throws CertificateException, NoSuchAlgorithmException,
            KeyStoreException, IOException, UnrecoverableKeyException, KeyManagementException {

        Objects.requireNonNull(certAuthorityFiles, "Could not load the CA cert file.  Please verify that "
                + "the certificate file is on the classpath or defined on the file system using the 'file://'"
                + "prefix.");
        Objects.requireNonNull(authCert, "Could not load the auth cert file.  Please verify that "
                + "the certificate file is on the classpath or defined on the file system using the "
                + "'file://' prefix.");

        Set<Certificate> caCertificates = getCertificates(certAuthorityFiles);

        KeyStore pkcs12KeyStore = getPkcs12KeyStore(authCert, authCertPassword);
        KeyStore sunKeyStore = getJksKeyStore(caCertificates, pkcs12KeyStore, authCertAlias, authCertPassword);
        SSLContext sslContext = getDefaultSSLContext(sunKeyStore, pkcs12KeyStore, authCertPassword);
        return sslContext.getSocketFactory();
    }

    private static Set<Certificate> getCertificates(List<InputStream> certAuthorityFiles) throws CertificateException {
        Set<Certificate> caCertificates = new LinkedHashSet<>();

        for(InputStream certAuthorityFile : certAuthorityFiles) {
            caCertificates.add(getCertificate(certAuthorityFile));
        }
        return caCertificates;
    }

    private static InputStream loadFile(String filePath) throws FileNotFoundException {
        return loadFile(SslSocketFactories.class.getClassLoader(), filePath);
    }

    private static InputStream loadFile(ClassLoader classLoader, String filePath) throws FileNotFoundException {
        if (filePath.startsWith(FILE_PROTOCOL)) {
            return new FileInputStream(filePath.substring(FILE_PROTOCOL.length()));
        }
        return classLoader.getResourceAsStream(filePath);
    }

    private static X509Certificate getCertificate(InputStream certificate)
            throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        return (X509Certificate) cf.generateCertificate(certificate);
    }

    private static KeyStore getPkcs12KeyStore(InputStream lightblueCert, char[] certPassword)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore ks = KeyStore.getInstance("pkcs12");
        ks.load(lightblueCert, certPassword);
        return ks;
    }

    private static KeyStore getJksKeyStore(Set<Certificate> caCertFiles, KeyStore lightblueCertKeystore,
                                           String lightblueCertAlias, char[] lightblueCertPassword)
            throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException,
            UnrecoverableKeyException {
        KeyStore jks = KeyStore.getInstance("jks");

        jks.load(null, lightblueCertPassword);
        for(Certificate caCertFile : caCertFiles) {
            jks.setCertificateEntry(caCertFile.toString(), caCertFile);
        }

        Certificate[] chain = lightblueCertKeystore.getCertificateChain(lightblueCertAlias);
        Key key = lightblueCertKeystore.getKey(lightblueCertAlias, lightblueCertPassword);
        jks.setKeyEntry("anykey", key, lightblueCertPassword, chain);

        return jks;
    }

    private static SSLContext getDefaultSSLContext(KeyStore trustKeyStore, KeyStore authKeyStore,
                                                   char[] authCertPassword)
            throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException,
            KeyManagementException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustKeyStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(authKeyStore, authCertPassword);

        SSLContext ctx = SSLContext.getInstance(TLSV1);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return ctx;
    }

    private static void validateLightblueClientConfigForCertAuth(LightblueClientConfiguration config) {
        if (config.getCaFilePath() == null) {
            throw new IllegalArgumentException("Must provide a caFilePath.");
        }
        if (config.getCertFilePath() == null) {
            throw new IllegalArgumentException("Must provide a certFilePath.");
        }
        if (config.getCertPassword() == null) {
            throw new IllegalArgumentException("Must provide a certPassword.");
        }
    }
}

package com.redhat.lightblue.client.http.auth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.lightblue.client.LightblueClientConfiguration;

/**
 * Provides access to certificates.
 *
 * @author mpatercz
 *
 */
public class CertificateManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SslSocketFactories.class);

    static final String FILE_PROTOCOL = "file://";

    static List<InputStream> getCaCertFiles(List<String> caCertFilePaths) throws FileNotFoundException {
        List<InputStream> caCertFiles = new ArrayList<>();

        for(String caCertFilePath : caCertFilePaths) {
            caCertFiles.add(loadFile(caCertFilePath));
        }

        return caCertFiles;
    }

    static Set<Certificate> getCertificates(List<InputStream> certAuthorityFiles) throws CertificateException {
        Set<Certificate> caCertificates = new LinkedHashSet<>();

        for(InputStream certAuthorityFile : certAuthorityFiles) {
            caCertificates.add(getCertificate(certAuthorityFile));
        }
        return caCertificates;
    }

    static InputStream loadFile(String filePath) throws FileNotFoundException {
        InputStream stream = loadFile(CertificateManager.class.getClassLoader(), filePath);
        if (stream == null) {
            throw new FileNotFoundException("Could not read certs from "+filePath);
        }
        return stream;
    }

    private static InputStream loadFile(ClassLoader classLoader, String filePath) throws FileNotFoundException {
        if (filePath.startsWith(FILE_PROTOCOL)) {
            return new FileInputStream(filePath.substring(FILE_PROTOCOL.length()));
        }
        return classLoader.getResourceAsStream(filePath);
    }

    static X509Certificate getCertificate(InputStream certificate)
            throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        return (X509Certificate) cf.generateCertificate(certificate);
    }

    static KeyStore getPkcs12KeyStore(InputStream lightblueCert, char[] certPassword)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore ks = KeyStore.getInstance("pkcs12");
        ks.load(lightblueCert, certPassword);
        return ks;
    }

    static KeyStore getJksKeyStore(Set<Certificate> caCertFiles, KeyStore lightblueCertKeystore,
                                           String lightblueCertAlias, char[] lightblueCertPassword)
            throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException,
            UnrecoverableKeyException {
        KeyStore jks = KeyStore.getInstance("jks");

        jks.load(null, lightblueCertPassword);
        for(Certificate caCertFile : caCertFiles) {
            jks.setCertificateEntry(caCertFile.toString(), caCertFile);
        }

        Certificate[] chain;
        Key key ;

        if (lightblueCertAlias != null) {
            LOGGER.debug("Loading certificates using alias='"+lightblueCertAlias+"'");
            chain = lightblueCertKeystore.getCertificateChain(lightblueCertAlias);
            key = lightblueCertKeystore.getKey(lightblueCertAlias, lightblueCertPassword);

            if (chain == null || key == null) {
                throw new RuntimeException("Specified alias='"+lightblueCertAlias+"' does not appear to exist in the keystore.");
            }
        } else {
            LOGGER.debug("Certificate alias not specified");

            List<String> aliases = Collections.list(lightblueCertKeystore.aliases());

            if (aliases.size() == 1) {
                String alias = aliases.get(0);
                LOGGER.debug("Certificate alias was not specified, but only one alias exist is the keystore. Using alias='"+alias+"'");
                chain = lightblueCertKeystore.getCertificateChain(alias);
                key = lightblueCertKeystore.getKey(alias, lightblueCertPassword);
            } else {
                throw new RuntimeException("Certificate alias not specified and the keystore has more than one alias or keystore is empty. Aliases found: "+aliases);
            }
        }

        jks.setKeyEntry("anykey", key, lightblueCertPassword, chain);

        return jks;
    }

}

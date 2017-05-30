package com.redhat.lightblue.client.http.auth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;



public class TestCertificateManager {

    String CACERT = "alias/cacert.pem";
    String AUTHCERT = "alias/authcert.pkcs12";
    String ALIAS = "Blah Blah";
    String PASSWORD = "foobar";

    @Test
    public void testCertConfigurationCorrect() throws Exception {
        loadCerts(CACERT, AUTHCERT, ALIAS, PASSWORD);
    }

    @Test
    public void testWrongPassword() throws Exception {
        try {
            loadCerts(CACERT, AUTHCERT, ALIAS, "wrongpassword");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("keystore password was incorrect") || e.getMessage().startsWith("failed to decrypt safe contents entry"));
        }
    }

    @Test
    public void testWrongAlias() throws Exception {
        try {
            loadCerts(CACERT, AUTHCERT, "wrongalias", PASSWORD);
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("Specified alias='wrongalias' does not appear to exist in the keystore.", e.getMessage());
        }
    }

    @Test
    public void testNoAliasWorksBecuaseThereIsOnlyOneCert() throws Exception {
        loadCerts(CACERT, AUTHCERT, null, PASSWORD);
    }

    @Test
    public void testWrongCacertPath() throws Exception {
        try {
            loadCerts("foo/bar.pem", AUTHCERT, ALIAS, PASSWORD);
            Assert.fail();
        } catch (FileNotFoundException e) {
            Assert.assertEquals("Could not read certs from foo/bar.pem", e.getMessage());
        }
    }

    @Test
    public void testWrongAuthCertPath() throws Exception {
        try {
            loadCerts(CACERT, "foo/bar.pkcs12", ALIAS, PASSWORD);
            Assert.fail();
        } catch (FileNotFoundException e) {
            Assert.assertEquals("Could not read certs from foo/bar.pkcs12", e.getMessage());
        }
    }

    void loadCerts(String cacertPath, String authCertPath, String alias, String password) throws Exception {
        Set<Certificate> certificates = CertificateManager.getCertificates(CertificateManager.getCaCertFiles(Arrays.asList(new String[]{ cacertPath })));
        KeyStore pkcs12KeyStore = CertificateManager.getPkcs12KeyStore(CertificateManager.loadFile(authCertPath), password.toCharArray());
        KeyStore sunKeyStore = CertificateManager.getJksKeyStore(certificates, pkcs12KeyStore, alias, password.toCharArray());
    }

}

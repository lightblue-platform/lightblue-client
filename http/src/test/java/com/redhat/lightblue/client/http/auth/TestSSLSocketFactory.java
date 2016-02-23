package com.redhat.lightblue.client.http.auth;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.redhat.lightblue.client.LightblueClientConfiguration;

public class TestSSLSocketFactory {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testFromLightblueClientConfig_MissingCaFilePath() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Must provide a caFilePath.");

        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setUseCertAuth(true);

        SslSocketFactories.fromLightblueClientConfig(config);
    }

    @Test
    public void testFromLightblueClientConfig_MissingCertFilePath() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Must provide a certFilePath.");

        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setUseCertAuth(true);
        config.setCaFilePath("/some/path/to/caFile.pem");

        SslSocketFactories.fromLightblueClientConfig(config);
    }

    @Test
    public void testFromLightblueClientConfig_MissingCertPassword() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Must provide a certPassword.");

        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setUseCertAuth(true);
        config.setCaFilePath("/some/path/to/caFile.pem");
        config.setCertFilePath("/some/path/to/certFile.pkcs12");

        SslSocketFactories.fromLightblueClientConfig(config);
    }

    @Test
    public void testJavaNetSslSocketFactory_MissingCaFilePath() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Must provide a caFilePath.");

        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setUseCertAuth(true);

        SslSocketFactories.javaNetSslSocketFactory(config);
    }

    @Test
    public void testJavaNetSslSocketFactory_MissingCertFilePath() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Must provide a certFilePath.");

        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setUseCertAuth(true);
        config.setCaFilePath("/some/path/to/caFile.pem");

        SslSocketFactories.javaNetSslSocketFactory(config);
    }

    @Test
    public void testJavaNetSslSocketFactory_MissingCertPassword() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Must provide a certPassword.");

        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setUseCertAuth(true);
        config.setCaFilePath("/some/path/to/caFile.pem");
        config.setCertFilePath("/some/path/to/certFile.pkcs12");

        SslSocketFactories.javaNetSslSocketFactory(config);
    }

}

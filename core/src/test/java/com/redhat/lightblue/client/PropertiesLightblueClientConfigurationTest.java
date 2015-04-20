package com.redhat.lightblue.client;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class PropertiesLightblueClientConfigurationTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldUseLightblueDashClientDotPropertiesAtRootOfClassPathByDefault() {
        LightblueClientConfiguration config = PropertiesLightblueClientConfiguration.fromDefault();

        // see src/test/resources/lightblue-client.properties
        assertEquals("http://this.is.a/test/metadata", config.getMetadataServiceURI());
    }

    @Test
    public void shouldThrowMeaningfulErrorMessageIfPropertiesCantBeReadViaInputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.toString()).thenReturn("stream.toString()");
        when(inputStream.read(any(byte[].class))).thenThrow(IOException.class);

        exception.expect(LightblueClientConfigurationException.class);
        exception.expectCause(CoreMatchers.<Throwable>instanceOf(IOException.class));
        exception.expectMessage(CoreMatchers.equalTo("Could not read properties file from input stream, " + inputStream));

        PropertiesLightblueClientConfiguration.fromInputStream(inputStream);
    }

    @Test
    public void shouldThrowMeaningfulErrorMessageIfPropertiesCantBeReadViaResource() {
        String bogusResource = "this.does.not.exist.properties";

        exception.expect(LightblueClientConfigurationException.class);
        exception.expectMessage(CoreMatchers.equalTo("Could not find properties resource at " +
                bogusResource));

        PropertiesLightblueClientConfiguration.fromResource(bogusResource);
    }

    @Test
    public void shouldThrowMeaningfulErrorMessageIfPropertiesCantBeReadViaPath() throws IOException {
        Path bogusPath = Paths.get("this.does.not.exist.properties");

        exception.expect(LightblueClientConfigurationException.class);
        exception.expectCause(CoreMatchers.<Throwable>instanceOf(IOException.class));
        exception.expectMessage(CoreMatchers.equalTo("Could not read properties file from path, " + bogusPath));

        PropertiesLightblueClientConfiguration.fromPath(bogusPath);
    }

    @Test
    public void shouldLookup_caFilePath_PropertyForCaFilePath() {
        Properties properties = new Properties();
        properties.setProperty("caFilePath", "theFilePath");

        LightblueClientConfiguration config = PropertiesLightblueClientConfiguration.fromObject(properties);

        assertEquals("theFilePath", config.getCaFilePath());
    }

    @Test
    public void shouldLookup_certFilePath_PropertyForCertFilePath() {
        Properties properties = new Properties();
        properties.setProperty("certFilePath", "theFilePath");

        LightblueClientConfiguration config = PropertiesLightblueClientConfiguration.fromObject(properties);

        assertEquals("theFilePath", config.getCertFilePath());
    }

    @Test
    public void shouldLookup_certPassword_PropertyForCertPassword() {
        Properties properties = new Properties();
        properties.setProperty("certPassword", "thePassword");

        LightblueClientConfiguration config = PropertiesLightblueClientConfiguration.fromObject(properties);

        assertEquals("thePassword", config.getCertPassword());
    }

    @Test
    public void shouldLookup_certAlias_PropertyForCertAlias() {
        Properties properties = new Properties();
        properties.setProperty("certFilePath", "/path/to/theCert.pkcs12"); 

        LightblueClientConfiguration config = PropertiesLightblueClientConfiguration.fromObject(properties);

        assertEquals("theCert", config.getCertAlias());
    }

    @Test
    public void shouldLookup_metadataServiceURI_PropertyForMetadataServiceUri() {
        Properties properties = new Properties();
        properties.setProperty("metadataServiceURI", "http://service.com/metadata");

        LightblueClientConfiguration config = PropertiesLightblueClientConfiguration.fromObject(properties);

        assertEquals("http://service.com/metadata", config.getMetadataServiceURI());
    }

    @Test
    public void shouldLookup_dataServiceURI_PropertyForDataServiceUri() {
        Properties properties = new Properties();
        properties.setProperty("dataServiceURI", "http://service.com/data");

        LightblueClientConfiguration config = PropertiesLightblueClientConfiguration.fromObject(properties);

        assertEquals("http://service.com/data", config.getDataServiceURI());
    }

    @Test
    public void shouldLookup_useCertAuth_PropertyForUseCertAuth() {
        Properties properties = new Properties();
        properties.setProperty("useCertAuth", "true");

        LightblueClientConfiguration config = PropertiesLightblueClientConfiguration.fromObject(properties);

        assertEquals(true, config.useCertAuth());
    }
}

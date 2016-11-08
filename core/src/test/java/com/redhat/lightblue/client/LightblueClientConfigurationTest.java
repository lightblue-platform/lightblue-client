package com.redhat.lightblue.client;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class LightblueClientConfigurationTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldCopyAllPropertiesInCopyConstructor() {
        LightblueClientConfiguration original = new LightblueClientConfiguration();

        original.setUseCertAuth(true);
        original.setMetadataServiceURI("metadata");
        original.setDataServiceURI("data");
        original.setCertPassword("pass");
        original.setCertFilePath("certpath");
        original.setCaFilePath("capath");

        LightblueClientConfiguration copy = new LightblueClientConfiguration(original);

        assertEquals(true, copy.useCertAuth());
        assertEquals("metadata", copy.getMetadataServiceURI());
        assertEquals("data", copy.getDataServiceURI());
        assertEquals("pass", copy.getCertPassword());
        assertEquals("certpath", copy.getCertFilePath());
        assertEquals("capath", copy.getCaFilePath());
        assertEquals("certpath", copy.getCertAlias());

        // make sure they are copies
        original.setUseCertAuth(false);
        original.setMetadataServiceURI("1metadata");
        original.setDataServiceURI("1data");
        original.setCertPassword("1pass");
        original.setCertFilePath("1certpath");
        original.setCaFilePath("1capath");

        assertEquals(true, copy.useCertAuth());
        assertEquals("metadata", copy.getMetadataServiceURI());
        assertEquals("data", copy.getDataServiceURI());
        assertEquals("pass", copy.getCertPassword());
        assertEquals("certpath", copy.getCertFilePath());
        assertEquals("capath", copy.getCaFilePath());
        assertEquals("certpath", copy.getCertAlias());
    }

    @Test
    public void getCaFilePathShouldThrowExceptionWhenThereAreMultipleValues() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("There are multiple CA cert paths defined, use getCaFilePaths() instead");

        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setCaFilePath("cacert1.pem,cacert2.pem");

        config.getCaFilePath();
    }

    @Test
    public void getCaFilePathShouldNotThrowExceptionWhenThereIsOnlyOneValue() throws Exception {
        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setCaFilePath("cacert1.pem");

        config.getCaFilePath();
    }

}

package com.redhat.lightblue.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class LightblueClientConfigurationTest {
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
}

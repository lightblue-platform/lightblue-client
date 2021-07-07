package com.redhat.lightblue.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by ykoer on 7/31/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class, SystemEnvironmentAwareProperties.class})
public class PropertiesLightblueClientSystemEnvTest {

    @Test
    public void shouldReplaceSystemEnvironmentPropertyPlaceholders() {

        PowerMockito.mockStatic(System.class);
        when(System.getenv("LB_DATA_SERVICE_URL")).thenReturn("http://this.is.a/test/data");
        when(System.getenv("LB_METADATA_SERVICE_URL")).thenReturn("http://this.is.a/test/metadata");

        Path resourceWithSystemEnvPlaceholders = Paths.get("lightblue-client-systemenv.properties");
        LightblueClientConfiguration config = PropertiesLightblueClientConfiguration.fromPath(resourceWithSystemEnvPlaceholders);

        assertEquals("http://this.is.a/test/data", config.getDataServiceURI());
        assertEquals("http://this.is.a/test/metadata", config.getMetadataServiceURI());

        // properties without placeholders should still work
        assertEquals("certFile", config.getCertAlias());
    }
}

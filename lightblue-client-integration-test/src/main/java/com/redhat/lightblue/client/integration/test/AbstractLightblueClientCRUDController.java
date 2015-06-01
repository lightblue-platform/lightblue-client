package com.redhat.lightblue.client.integration.test;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.LightblueClientConfiguration.Compression;
import com.redhat.lightblue.client.http.LightblueHttpClient;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.client.test.request.DataInsertRequestStub;
import com.redhat.lightblue.test.utils.AbstractCRUDControllerWithRest;

/**
 * Provides a lightblue-client instance to talk to the running in-memory lightblue instance.
 *
 * @author dcrissman
 */
public abstract class AbstractLightblueClientCRUDController extends AbstractCRUDControllerWithRest {

    public AbstractLightblueClientCRUDController() throws Exception {
        super();
        setupSystemProperties();
    }

    public AbstractLightblueClientCRUDController(int httpServerPort) throws Exception {
        super(httpServerPort);
        setupSystemProperties();
    }

    private void setupSystemProperties() {
        System.setProperty("client.data.url", getDataUrl());
        System.setProperty("client.metadata.url", getMetadataUrl());
    }

    /**
     *
     * @return lightblue http client configuration needed to connect
     */
    @Override
    protected LightblueClientConfiguration getLightblueClientConfiguration() {
        LightblueClientConfiguration lbConf = new LightblueClientConfiguration();
        lbConf.setUseCertAuth(false);
        lbConf.setDataServiceURI(getDataUrl());
        lbConf.setMetadataServiceURI(getMetadataUrl());
        lbConf.setCompression(Compression.NONE);
        return lbConf;
    }

    @Override
    protected LightblueHttpClient getLightblueClient() {
        return new LightblueHttpClient(getLightblueClientConfiguration());
    }

    @Override
    protected LightblueResponse loadData(String entityName, String entityVersion, String resourcePath) throws IOException {
        DataInsertRequestStub request = new DataInsertRequestStub(
                entityName, entityVersion, loadResource(resourcePath, false));
        LightblueResponse response = getLightblueClient().data(request);
        assertFalse(response.getText(), response.hasError());

        return response;
    }

}

package com.redhat.lightblue.client.integration.test;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadResource;

import java.io.IOException;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.LightblueHttpClient;
import com.redhat.lightblue.client.response.LightblueException;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.client.test.request.DataInsertRequestStub;
import com.redhat.lightblue.rest.integration.AbstractCRUDControllerWithRest;

/**
 * Provides a lightblue-client instance to talk to the running in-memory lightblue instance.
 *
 * @author dcrissman
 */
public abstract class LightblueClientTestHarness extends AbstractCRUDControllerWithRest {

    public LightblueClientTestHarness() throws Exception {
        super();
        setupSystemProperties();
    }

    public LightblueClientTestHarness(int httpServerPort) throws Exception {
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
    protected LightblueClientConfiguration getLightblueClientConfiguration() {
        LightblueClientConfiguration lbConf = new LightblueClientConfiguration();
        lbConf.setUseCertAuth(false);
        lbConf.setDataServiceURI(getDataUrl());
        lbConf.setMetadataServiceURI(getMetadataUrl());
        return lbConf;
    }

    public LightblueHttpClient getLightblueClient() {
        return new LightblueHttpClient(getLightblueClientConfiguration());
    }

    public LightblueResponse loadData(String entityName, String entityVersion, String resourcePath) throws LightblueException, IOException {
        DataInsertRequestStub request = new DataInsertRequestStub(
                entityName, entityVersion, loadResource(resourcePath));
        return getLightblueClient().data(request);
    }

}

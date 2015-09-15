package com.redhat.lightblue.client.integration.test;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadResource;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.LightblueHttpClient;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityVersionsRequest;
import com.redhat.lightblue.client.response.LightblueException;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.client.test.request.DataInsertRequestStub;
import com.redhat.lightblue.rest.integration.AbstractCRUDControllerWithRest;

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

    /**
     * Helper method to be able to collect all the versions of the provided entity that currently exist in lightblue.
     * @param entityName - name of entity to to find versions for.
     * @return Set of entity versions
     * @throws LightblueException
     */
    public Set<String> getEntityVersions(String entityName) throws LightblueException {
        MetadataGetEntityVersionsRequest versionRequest = new MetadataGetEntityVersionsRequest(entityName);

        LightblueResponse response = getLightblueClient().metadata(versionRequest);
        ArrayNode versionNodes = (ArrayNode) response.getJson();

        Set<String> versions = new HashSet<>();

        for (JsonNode node : versionNodes) {
            versions.add(node.get("version").textValue());
        }

        return versions;
    }

}

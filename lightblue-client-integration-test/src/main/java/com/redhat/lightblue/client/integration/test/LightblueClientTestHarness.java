package com.redhat.lightblue.client.integration.test;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadResource;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.http.LightblueHttpClient;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityVersionsRequest;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.rest.integration.LightblueRestTestHarness;

import io.undertow.security.idm.IdentityManager;

/**
 * Provides a lightblue-client instance to talk to the running in-memory
 * lightblue instance.
 *
 * @author dcrissman
 */
public abstract class LightblueClientTestHarness extends LightblueRestTestHarness {

    public LightblueClientTestHarness() throws Exception {
        super();
        setupSystemProperties();
    }

    public LightblueClientTestHarness(int httpServerPort) throws Exception {
        super(httpServerPort);
        setupSystemProperties();
    }

    public LightblueClientTestHarness(int httpServerPort, IdentityManager identityManager) throws Exception {
        super(httpServerPort, identityManager);
        setupSystemProperties();
    }

    private void setupSystemProperties() {
        System.setProperty("client.data.url", getDataUrl());
        System.setProperty("client.metadata.url", getMetadataUrl());
    }

    /**
     * @return lightblue http noauth client configuration needed to connect
     */
    public LightblueClientConfiguration getLightblueClientConfiguration() {
        LightblueClientConfiguration lbConf = new LightblueClientConfiguration();
        lbConf.setUseCertAuth(false);
        lbConf.setDataServiceURI(getDataUrl());
        lbConf.setMetadataServiceURI(getMetadataUrl());
        return lbConf;
    }

    /**
     * @param username - principal user name
     * @param password - password for principal
     * @return lightblue http basic auth client configuration needed to connect
     */
    public LightblueClientConfiguration getLightblueClientConfiguration(String username, String password) {
        LightblueClientConfiguration lbConf = new LightblueClientConfiguration();
        lbConf.setUseCertAuth(false);
        lbConf.setBasicAuthUsername(username);
        lbConf.setBasicAuthPassword(password);
        lbConf.setDataServiceURI(getDataUrl());
        lbConf.setMetadataServiceURI(getMetadataUrl());
        return lbConf;
    }

    /**
     * Default implementation returns a client using no auth.
     * @return {@link LightblueHttpClient}
     */
    public LightblueHttpClient getLightblueClient() {
        return getLightblueClient(getLightblueClientConfiguration());
    }

    public LightblueHttpClient getLightblueClient(LightblueClientConfiguration config) {
        return new LightblueHttpClient(config);
    }

    public LightblueResponse loadData(String entityName, String entityVersion, String resourcePath) throws LightblueException, IOException {
        final String data = loadResource(resourcePath);
        DataInsertRequest request = new DataInsertRequest(entityName, entityVersion) {

            private static final long serialVersionUID = -1041659993686116395L;

            @Override
            public String getBody() {
                return data;
            }
        };
        try (LightblueHttpClient client = getLightblueClient()) {
            return client.data(request);
        }
    }

    /**
     * Helper method to be able to collect all the versions of the provided
     * entity that currently exist in lightblue.
     *
     * @param entityName - name of entity to to find versions for.
     * @return Set of entity versions
     * @throws LightblueException
     */
    public Set<String> getEntityVersions(String entityName) throws LightblueException {
        MetadataGetEntityVersionsRequest versionRequest = new MetadataGetEntityVersionsRequest(entityName);

        try (LightblueHttpClient client = getLightblueClient()) {
            LightblueResponse response = client.metadata(versionRequest);
            ArrayNode versionNodes = (ArrayNode) response.getJson();

            Set<String> versions = new HashSet<>();

            for (JsonNode node : versionNodes) {
                versions.add(node.get("version").textValue());
            }

            return versions;
        } catch (IOException ex) {
            throw new LightblueException("Unable to close HttpClient", ex);
        }
    }

}

package com.redhat.lightblue.client.integration.test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.annotation.Obsolete;
import org.junit.runners.model.TestClass;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.response.LightblueResponse;

public class LightblueExternalResource extends BeforeAfterTestRule {

    @Obsolete
    public interface LightblueTestMethods extends LightblueTestHarnessConfig {}

    public interface LightblueTestHarnessConfig {
        JsonNode[] getMetadataJsonNodes() throws Exception;
    }

    private final LightblueTestMethods methods;
    private final int httpServerPort;
    private boolean removeHooks = Boolean.TRUE;

    private ArtificialLightblueClientCRUDController controller;

    public LightblueExternalResource(LightblueTestMethods methods) {
        this(methods, 8000);
    }

    public LightblueExternalResource(LightblueTestMethods methods,boolean removeHooks) {
        this(methods, 8000);
        this.removeHooks = removeHooks;
    }

    public LightblueExternalResource(LightblueTestMethods methods, Integer httpServerPort) {
        super(new TestClass(ArtificialLightblueClientCRUDController.class));

        if (methods == null) {
            throw new IllegalArgumentException("Must provide an instance of LightblueTestMethods");
        }
        this.methods = methods;
        this.httpServerPort = httpServerPort;
    }

    protected LightblueClientTestHarness getControllerInstance() {
        if (controller == null) {
            try {
                if (removeHooks) {
                    controller = new ArtificialLightblueClientCRUDController(httpServerPort);
                } else {
                    controller = new ArtificialLightblueClientCRUDControllerWithHooks(httpServerPort);
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to create test CRUD Controller", e);
            }
        }
        return controller;
    }

    public LightblueClient getLightblueClient() {
        return getControllerInstance().getLightblueClient();
    }

    public LightblueResponse loadData(String entityName, String entityVersion, String resourcePath) throws IOException, LightblueException {
        return getControllerInstance().loadData(entityName, entityVersion,
                resourcePath);
    }

    public Set<String> getEntityVersions(String entityName) throws LightblueException {
        return getControllerInstance().getEntityVersions(entityName);
    }

    public void cleanupMongoCollections(String... collectionNames) throws UnknownHostException {
        getControllerInstance().cleanupMongoCollections(collectionNames);
    }

    public void cleanupMongoCollections(String dbName, String[] collectionNames) throws UnknownHostException {
        getControllerInstance().cleanupMongoCollections(dbName, collectionNames);
    }

    public int getHttpPort() {
        return getControllerInstance().getHttpPort();
    }

    public String getDataUrl() {
        return getControllerInstance().getDataUrl();
    }

    public String getMetadataUrl() {
        return getControllerInstance().getMetadataUrl();
    }

    private class ArtificialLightblueClientCRUDController extends LightblueClientTestHarness {

        public ArtificialLightblueClientCRUDController(int httpServerPort) throws Exception {
            super(httpServerPort);
        }

        @Override
        protected JsonNode[] getMetadataJsonNodes() throws Exception {
            return methods.getMetadataJsonNodes();
        }

    }

    private class ArtificialLightblueClientCRUDControllerWithHooks extends ArtificialLightblueClientCRUDController {

        public ArtificialLightblueClientCRUDControllerWithHooks(int httpServerPort) throws Exception {
            super(httpServerPort);
        }

        @Override
        public Set<String> getHooksToRemove() {
            return new HashSet<>();
        }


    }


}

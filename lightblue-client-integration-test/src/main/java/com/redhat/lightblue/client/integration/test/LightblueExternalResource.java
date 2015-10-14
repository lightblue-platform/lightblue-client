package com.redhat.lightblue.client.integration.test;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.runners.model.TestClass;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.response.LightblueException;
import com.redhat.lightblue.client.response.LightblueResponse;

public class LightblueExternalResource extends BeforeAfterTestRule {

    public interface LightblueTestMethods {
        JsonNode[] getMetadataJsonNodes() throws Exception;
    }

    private final LightblueTestMethods methods;
    private final int httpServerPort;

    private ArtificialLightblueClientCRUDController controller;

    public LightblueExternalResource(LightblueTestMethods methods) {
        this(methods, 8000);
    }

    public LightblueExternalResource(LightblueTestMethods methods, Integer httpServerPort) {
        super(new TestClass(ArtificialLightblueClientCRUDController.class));

        if (methods == null) {
            throw new IllegalArgumentException("Must provide an instance of LightblueTestMethods");
        }
        this.methods = methods;
        this.httpServerPort = httpServerPort;
    }

    protected AbstractLightblueClientCRUDController getControllerInstance() {
        if (controller == null) {
            try {
                controller = new ArtificialLightblueClientCRUDController(httpServerPort);
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

    private class ArtificialLightblueClientCRUDController extends AbstractLightblueClientCRUDController {

        public ArtificialLightblueClientCRUDController(int httpServerPort) throws Exception {
            super(httpServerPort);
        }

        @Override
        protected JsonNode[] getMetadataJsonNodes() throws Exception {
            return methods.getMetadataJsonNodes();
        }

    }

}

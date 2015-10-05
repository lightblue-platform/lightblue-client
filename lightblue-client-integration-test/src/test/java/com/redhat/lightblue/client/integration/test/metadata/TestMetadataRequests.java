package com.redhat.lightblue.client.integration.test.metadata;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.enums.MetadataStatus;
import com.redhat.lightblue.client.integration.test.AbstractLightblueClientCRUDController;
import com.redhat.lightblue.client.request.metadata.MetadataClearDefaultVersionRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityDependenciesRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityMetadataRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityNamesRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityRolesRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityVersionsRequest;
import com.redhat.lightblue.client.request.metadata.MetadataSetDefaultVersionRequest;
import com.redhat.lightblue.client.request.metadata.MetadataUpdateSchemaStatusRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public class TestMetadataRequests extends AbstractLightblueClientCRUDController {

    public TestMetadataRequests() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{loadJsonNode("./metadata/country.json")};
    }

    @Test
    public void testMetadataGetEntityNamesRequest() {
        MetadataGetEntityNamesRequest request = new MetadataGetEntityNamesRequest();

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataGetEntityVersionsRequest() {
        MetadataGetEntityVersionsRequest request = new MetadataGetEntityVersionsRequest("country");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataGetEntityMetadataRequest() {
        MetadataGetEntityMetadataRequest request = new MetadataGetEntityMetadataRequest("country", "0.1.0-SNAPSHOT");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataGetEntityDependenciesRequest() {
        MetadataGetEntityDependenciesRequest request = new MetadataGetEntityDependenciesRequest("country", "0.1.0-SNAPSHOT");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataGetEntityRolesRequest() {
        getLightblueClient().metadata(new MetadataSetDefaultVersionRequest("country", "0.1.0-SNAPSHOT"));
        
        MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest();

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
        
        getLightblueClient().metadata(new MetadataClearDefaultVersionRequest("country"));
    }

    @Test
    public void testMetadataGetEntityRolesRequest_WithEntity() {
        getLightblueClient().metadata(new MetadataSetDefaultVersionRequest("country", "0.1.0-SNAPSHOT"));
        
        MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest("country");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
        
        getLightblueClient().metadata(new MetadataClearDefaultVersionRequest("country"));
    }

    @Test
    public void testMetadataGetEntityRolesRequest_WithEntityAndVersion() {
        MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest("country", "0.1.0-SNAPSHOT");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataClearDefaultVersionRequest() {
        getLightblueClient().metadata(new MetadataSetDefaultVersionRequest("country", "0.1.0-SNAPSHOT"));
        
        MetadataClearDefaultVersionRequest request = new MetadataClearDefaultVersionRequest("country");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataSetDefaultVersionRequest() {
        MetadataSetDefaultVersionRequest request = new MetadataSetDefaultVersionRequest("country", "0.1.0-SNAPSHOT");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
        
        getLightblueClient().metadata(new MetadataClearDefaultVersionRequest("country"));
    }

    @Test
    public void testMetadataUpdateSchemaStatusRequest() {
        MetadataUpdateSchemaStatusRequest request = new MetadataUpdateSchemaStatusRequest("country", "0.1.0-SNAPSHOT", MetadataStatus.DISABLED, "why not");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

}

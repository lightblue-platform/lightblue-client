package com.redhat.lightblue.client.integration.test.metadata;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.enums.MetadataStatus;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.request.metadata.MetadataClearDefaultVersionRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityDependenciesRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityMetadataRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityNamesRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityRolesRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityVersionsRequest;
import com.redhat.lightblue.client.request.metadata.MetadataSetDefaultVersionRequest;
import com.redhat.lightblue.client.request.metadata.MetadataUpdateSchemaStatusRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public class TestMetadataRequests extends LightblueClientTestHarness {

    public TestMetadataRequests() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{loadJsonNode("./metadata/country.json")};
    }

    @Test
    public void testMetadataGetEntityNamesRequest() throws LightblueException {
        MetadataGetEntityNamesRequest request = new MetadataGetEntityNamesRequest();

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataGetEntityVersionsRequest() throws LightblueException {
        MetadataGetEntityVersionsRequest request = new MetadataGetEntityVersionsRequest("country");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataGetEntityMetadataRequest() throws LightblueException {
        MetadataGetEntityMetadataRequest request = new MetadataGetEntityMetadataRequest("country", "0.1.0-SNAPSHOT");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataGetEntityDependenciesRequest() throws LightblueException {
        MetadataGetEntityDependenciesRequest request = new MetadataGetEntityDependenciesRequest("country", "0.1.0-SNAPSHOT");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataGetEntityRolesRequest() throws LightblueException {
        getLightblueClient().metadata(new MetadataSetDefaultVersionRequest("country", "0.1.0-SNAPSHOT"));

        MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest();

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);

        getLightblueClient().metadata(new MetadataClearDefaultVersionRequest("country"));
    }

    @Test
    public void testMetadataGetEntityRolesRequest_WithEntity() throws LightblueException {
        getLightblueClient().metadata(new MetadataSetDefaultVersionRequest("country", "0.1.0-SNAPSHOT"));

        MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest("country");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);

        getLightblueClient().metadata(new MetadataClearDefaultVersionRequest("country"));
    }

    @Test
    public void testMetadataGetEntityRolesRequest_WithEntityAndVersion() throws LightblueException {
        MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest("country", "0.1.0-SNAPSHOT");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataClearDefaultVersionRequest() throws LightblueException {
        getLightblueClient().metadata(new MetadataSetDefaultVersionRequest("country", "0.1.0-SNAPSHOT"));

        MetadataClearDefaultVersionRequest request = new MetadataClearDefaultVersionRequest("country");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

    @Test
    public void testMetadataSetDefaultVersionRequest() throws LightblueException {
        MetadataSetDefaultVersionRequest request = new MetadataSetDefaultVersionRequest("country", "0.1.0-SNAPSHOT");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);

        getLightblueClient().metadata(new MetadataClearDefaultVersionRequest("country"));
    }

    @Test
    public void testMetadataUpdateSchemaStatusRequest() throws LightblueException {
        MetadataUpdateSchemaStatusRequest request = new MetadataUpdateSchemaStatusRequest("country", "0.1.0-SNAPSHOT", MetadataStatus.DISABLED, "why not");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

}

package com.redhat.lightblue.client.response;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.util.JSON;

public class TestDefaultLightblueDiagnosticsResponse {
    
    private static final String diagnosticsSuccessResponse = 
            "{\"MongoCRUDController\":"
            + "{\"healthy\":true,\"message\":\""
            + "[Mongo Config [MongoURL, DatabaseName: metadata]=>ping:OK, "
            + "Mongo Config [MongoURL, DatabaseName: data]=>ping:OK]\"},"
            + "\"ldap-auth-healthcheck\":{\"healthy\":true,"
            + "\"message\":\"LDAPConnection [DN: uid=lightblueapp,ou=serviceusers,ou=lightblue,dc=redhat,dc=com, "
            + "Status: true]\"}}";
    
    private static final String diagnosticsFailureResponse = 
            "{\"MongoCRUDController\":"
            + "{\"healthy\":false,\"message\":\""
            + "[Mongo Config [MongoURL, DatabaseName: metadata]=>ping:OK, "
            + "Mongo Config [MongoURL, DatabaseName: data]=>ping:OK]\"},"
            + "\"ldap-auth-healthcheck\":{\"healthy\":true,"
            + "\"message\":\"LDAPConnection [DN: uid=lightblueapp,ou=serviceusers,ou=lightblue,dc=redhat,dc=com, "
            + "Status: true]\"}}";

    @Test(expected = LightblueException.class)
    public void testConstructor_BadJson() throws LightblueException {
        new DefaultLightblueDiagnosticsResponse("bad json", null);
    }
    
    @Test
    public void testSetText() throws Exception {
        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(diagnosticsSuccessResponse, testResponse.getText());
    }
    
    @Test
    public void testSetJson() throws Exception {
        ObjectMapper mapper = JSON.getDefaultObjectMapper();
        JsonNode node = mapper.readTree(diagnosticsSuccessResponse);

        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(node, testResponse.getJson());
    }
    
    @Test
    public void testHasDiagnostics() throws Exception {
        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(true, testResponse.hasDiagnostics("MongoCRUDController"));
    }
    
    @Test
    public void testDiagnostics() throws Exception {
        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        
        DiagnosticsElement element = testResponse.getDiagnostics("MongoCRUDController");
        Assert.assertEquals(true, element.isHealthy());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testDiagnostics_ElementNotPresent() throws Exception {
        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        
        testResponse.getDiagnostics("LdapCRUDController");
    }
    
    @Test
    public void testAllDiagnostics() throws Exception {
        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(2, testResponse.getDiagnostics().size());
    }
    
    @Test
    public void testAllHealthy() throws Exception {
        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(true, testResponse.allHealthy());
    }
    
    @Test
    public void testAllNotHealthy() throws Exception {
        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsFailureResponse, null);
        Assert.assertEquals(false, testResponse.allHealthy());
    }
}

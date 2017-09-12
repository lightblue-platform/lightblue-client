package com.redhat.lightblue.client.response;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.util.JSON;

public class TestDefaultLightblueDiagnosticsResponse {

    @Test(expected = LightblueException.class)
    public void testConstructor_BadJson() throws LightblueException {
        new DefaultLightblueDiagnosticsResponse("bad json", null);
    }
    
    @Test
    public void testSetText() throws Exception {
        URI uri = this.getClass().getClassLoader().getResource("diagnostics/lightblue-healthy-response.json").toURI();
        String diagnosticsSuccessResponse = new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));

        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(diagnosticsSuccessResponse, testResponse.getText());
    }
    
    @Test
    public void testSetJson() throws Exception {
        URI uri = this.getClass().getClassLoader().getResource("diagnostics/lightblue-healthy-response.json").toURI();
        String diagnosticsSuccessResponse = new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));

        ObjectMapper mapper = JSON.getDefaultObjectMapper();
        JsonNode node = mapper.readTree(diagnosticsSuccessResponse);

        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(node, testResponse.getJson());
    }
    
    @Test
    public void testHasDiagnostics() throws Exception {
        URI uri = this.getClass().getClassLoader().getResource("diagnostics/lightblue-healthy-response.json").toURI();
        String diagnosticsSuccessResponse = new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));

        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(true, testResponse.hasDiagnostics("MongoCRUDController"));
    }
    
    @Test
    public void testDiagnostics() throws Exception {
        URI uri = this.getClass().getClassLoader().getResource("diagnostics/lightblue-healthy-response.json").toURI();
        String diagnosticsSuccessResponse = new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));

        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        
        DiagnosticsElement element = testResponse.getDiagnostics("MongoCRUDController");        
        Assert.assertEquals(true, element.isHealthy());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testDiagnostics_ElementNotPresent() throws Exception {
        URI uri = this.getClass().getClassLoader().getResource("diagnostics/lightblue-healthy-response.json").toURI();
        String diagnosticsSuccessResponse = new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));

        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        
        testResponse.getDiagnostics("LdapCRUDController");
    }
    
    @Test
    public void testAllDiagnostics() throws Exception {
        URI uri = this.getClass().getClassLoader().getResource("diagnostics/lightblue-healthy-response.json").toURI();
        String diagnosticsSuccessResponse = new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));

        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(2, testResponse.getDiagnostics().size());
    }
    
    @Test
    public void testAllHealthy() throws Exception {
        URI uri = this.getClass().getClassLoader().getResource("diagnostics/lightblue-healthy-response.json").toURI();
        String diagnosticsSuccessResponse = new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));

        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsSuccessResponse, null);
        Assert.assertEquals(true, testResponse.allHealthy());
    }
    
    @Test
    public void testAllNotHealthy() throws Exception {
        URI uri = this.getClass().getClassLoader().getResource("diagnostics/lightblue-unhealthy-response.json").toURI();
        String diagnosticsFailureResponse = new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));

        DefaultLightblueDiagnosticsResponse testResponse = new DefaultLightblueDiagnosticsResponse(diagnosticsFailureResponse, null);
        Assert.assertEquals(false, testResponse.allHealthy());
    }
}
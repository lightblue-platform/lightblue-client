package com.redhat.lightblue.client.response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestLightblueResponse {

    LightblueResponse testResponse = new LightblueResponse();

    private static final String initialResponseText = "{\"name\":\"value\"}";
    private static final String updatedResponseText = "{\"name\":\"value\"}";

    @Before
    public void setUp() throws Exception {
        testResponse = new LightblueResponse(initialResponseText);
    }

    @Test
    public void testGetText() {
        Assert.assertEquals(initialResponseText, testResponse.getText());
    }

    @Test
    public void testSetText() {
        testResponse.setText(updatedResponseText);
        Assert.assertEquals(updatedResponseText, testResponse.getText());
    }

    @Test
    public void testSetJson() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(updatedResponseText);

        testResponse.setJson(node);
        Assert.assertEquals(node, testResponse.getJson());
    }

    @Test
    public void testHasError_NoErrorElement_False(){
        assertFalse(testResponse.hasError());
    }

    @Test
    public void testHasError_False(){
        LightblueResponse response = new LightblueResponse("{\"status\":\"successful\"}");
        assertFalse(response.hasError());
    }

    @Test
    public void testHasError_True(){
        LightblueResponse response = new LightblueResponse("{\"status\":\"error\"}");
        assertTrue(response.hasError());
    }

}

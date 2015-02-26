package com.redhat.lightblue.client.response;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LightblueResponse {

    private String text;
    private JsonNode json;

    public LightblueResponse() {

    }

    public LightblueResponse(String responseText) {
        this.text = responseText;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.readTree(responseText);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse response: " + responseText, e);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JsonNode getJson() {
        return json;
    }

    public void setJson(JsonNode json) {
        this.json = json;
    }

}

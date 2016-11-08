package com.redhat.lightblue.client.http.testing.doubles;

import com.fasterxml.jackson.databind.JsonNode;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.util.JSON;

public class FakeLightblueRequest extends LightblueRequest {
    private final String body;
    private final String path;

    public FakeLightblueRequest(String body, HttpMethod method, String path) {
        super(method);
        this.body = body;
        this.path = path;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public JsonNode getBodyJson() {
        return JSON.toJsonNode(body);
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        return baseServiceURI + path;
    }
}

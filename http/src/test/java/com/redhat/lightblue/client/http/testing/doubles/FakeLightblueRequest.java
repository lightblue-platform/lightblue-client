package com.redhat.lightblue.client.http.testing.doubles;
import com.fasterxml.jackson.databind.JsonNode;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.util.JSON;

public class FakeLightblueRequest implements LightblueRequest {
    private final String body;
    private final HttpMethod method;
    private final String path;

    public FakeLightblueRequest(String body, HttpMethod method, String path) {
        this.body = body;
        this.method = method;
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
    public HttpMethod getHttpMethod() {
        return method;
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        return baseServiceURI + path;
    }
}

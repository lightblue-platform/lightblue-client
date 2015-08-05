package com.redhat.lightblue.client.request;

import com.fasterxml.jackson.databind.JsonNode;

import com.redhat.lightblue.client.http.HttpMethod;

/**
 * Encapsulates an HTTP request to be used with a Lightblue HTTP service. This means that the body,
 * if present, will be UTF-8 JSON.
 */
public interface LightblueRequest {
    JsonNode getBodyJson();
    
    String getBody();

    HttpMethod getHttpMethod();

    String getRestURI(String baseServiceURI);
}

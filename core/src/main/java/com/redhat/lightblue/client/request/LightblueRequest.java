package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.http.HttpMethod;

public interface LightblueRequest {
    String getBody();

    HttpMethod getHttpMethod();

    String getRestURI(String baseServiceURI);
}

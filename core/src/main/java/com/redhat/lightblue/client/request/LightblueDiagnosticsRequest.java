package com.redhat.lightblue.client.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;

public class LightblueDiagnosticsRequest extends LightblueRequest{

    private static final long serialVersionUID = 2479314164852663290L;
    

    public LightblueDiagnosticsRequest() {
        super(HttpMethod.GET);
    }
    
    @Override
    public JsonNode getBodyJson() {
        return null;
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        StringBuilder requestURI = new StringBuilder();
        requestURI.append(baseServiceURI);
        LightblueRequest.appendToURI(requestURI, "diagnostics");
        return requestURI.toString();
    }
}

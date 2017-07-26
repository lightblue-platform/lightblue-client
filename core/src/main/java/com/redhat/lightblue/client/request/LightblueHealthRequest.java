package com.redhat.lightblue.client.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;

public class LightblueHealthRequest extends LightblueRequest{

    private static final long serialVersionUID = 2479314164852663290L;
    
    private String diagnosticsUri;

    public LightblueHealthRequest() {
        this("diagnostics");
    }
    
    public LightblueHealthRequest(String diagnosticsUri) {
        super(HttpMethod.GET);
        this.diagnosticsUri = diagnosticsUri;    
    }

    @Override
    public JsonNode getBodyJson() {
        return null;
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        StringBuilder requestURI = new StringBuilder();
        requestURI.append(baseServiceURI);
        LightblueRequest.appendToURI(requestURI, diagnosticsUri);
        return requestURI.toString();
    }

    public String getDiagnosticsUri() {
        return diagnosticsUri;
    }

    public void setDiagnosticsUri(String diagnosticsUri) {
        this.diagnosticsUri = diagnosticsUri;
    }
}

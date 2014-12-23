package com.redhat.lightblue.client.http.servlet;

public class LightblueMetadataProxyServlet extends PropertiesLightblueProxyServlet {
    @Override
    protected String baseServiceUri() {
        return configuration().getMetadataServiceURI().replaceAll("/+$", "");
    }
}

package com.redhat.lightblue.client.http;

public class LightblueMetadataProxyServlet extends AbstractLightblueProxyServlet {
    @Override
    protected String serviceUri() {
        return configuration.getMetadataServiceURI().replaceAll("/+$", "");
    }
}

package com.redhat.lightblue.client.http;

public class LightblueDataProxyServlet extends AbstractLightblueProxyServlet {
    @Override
    protected String serviceUri() {
        return configuration.getDataServiceURI().replaceAll("/+$", "");
    }
}

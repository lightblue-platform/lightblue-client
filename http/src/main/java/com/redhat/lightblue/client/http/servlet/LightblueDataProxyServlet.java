package com.redhat.lightblue.client.http.servlet;

public class LightblueDataProxyServlet extends PropertiesLightblueProxyServlet {
    @Override
    protected String baseServiceUri() {
        return configuration().getDataServiceURI().replaceAll("/+$", "");
    }
}

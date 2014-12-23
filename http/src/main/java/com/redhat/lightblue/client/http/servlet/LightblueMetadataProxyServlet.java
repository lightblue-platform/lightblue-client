package com.redhat.lightblue.client.http.servlet;

import javax.servlet.http.HttpServletRequest;

public final class LightblueMetadataProxyServlet extends PropertiesLightblueProxyServlet {
    @Override
    protected String serviceUriForRequest(HttpServletRequest request) {
        return configuration().getMetadataServiceURI().replaceAll("/+$", "") +
                servicePathForRequest(request);
    }
}

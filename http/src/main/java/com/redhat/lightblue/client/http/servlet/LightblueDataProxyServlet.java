package com.redhat.lightblue.client.http.servlet;

import javax.servlet.http.HttpServletRequest;

public final class LightblueDataProxyServlet extends PropertiesLightblueProxyServlet {
    @Override
    protected String serviceUriForRequest(HttpServletRequest request) {
        return configuration().getDataServiceURI().replaceAll("/+$", "") +
                servicePathForRequest(request);
    }
}

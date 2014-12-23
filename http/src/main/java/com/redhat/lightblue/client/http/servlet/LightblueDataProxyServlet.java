package com.redhat.lightblue.client.http.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public final class LightblueDataProxyServlet extends PropertiesLightblueProxyServlet {
    private String dataServiceUri;

    @Override
    public void init() throws ServletException {
        dataServiceUri = configuration().getDataServiceURI();

        if (dataServiceUri == null) {
            throw new LightblueServletException("No dataServiceURI defined in configuration, " +
                    configuration());
        }

        // Get rid of trailing slashes.
        dataServiceUri = dataServiceUri.replaceAll("/+$", "");
    }

    @Override
    protected String serviceUriForRequest(HttpServletRequest request) {
        return dataServiceUri + servicePathForRequest(request);
    }
}

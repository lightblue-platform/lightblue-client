package com.redhat.lightblue.client.http.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public final class LightblueMetadataProxyServlet extends PropertiesLightblueProxyServlet {
    private String metadataServiceUri;

    @Override
    public void init() throws ServletException {
        metadataServiceUri= configuration().getMetadataServiceURI();

        if (metadataServiceUri == null) {
            throw new LightblueServletException("No metadataServiceURI defined in configuration, " +
                    configuration());
        }

        // Get rid of trailing slashes.
        metadataServiceUri = metadataServiceUri.replaceAll("/+$", "");
    }

    @Override
    protected String serviceUriForRequest(HttpServletRequest request) {
        return metadataServiceUri + servicePathForRequest(request);
    }
}

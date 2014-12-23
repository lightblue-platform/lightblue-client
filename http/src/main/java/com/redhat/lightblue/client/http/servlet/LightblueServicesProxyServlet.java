package com.redhat.lightblue.client.http.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class LightblueServicesProxyServlet extends PropertiesLightblueProxyServlet {
    private String dataServicePath;
    private String metadataServicePath;

    @Override
    public void init() throws ServletException {
        super.init();

        dataServicePath = getInitParamWithDefault("dataServicePath", "/data").replaceAll("/+$", "");
        metadataServicePath = getInitParamWithDefault("metadataServicePath", "/metadata")
                .replaceAll("/+$", "");
    }

    @Override
    protected String serviceUriForRequest(HttpServletRequest request) throws ServletException {
        if (firstPathSegment(request).equals(dataServicePath)) {
            return dataServicePath + servicePathForRequest(request);
        }

        if (firstPathSegment(request).equals(metadataServicePath)) {
            return metadataServicePath + servicePathForRequest(request);
        }

        throw new LightblueServletException("Could not match request path to data service or " +
                "metadata service.\n" +
                "Path was: " + request.getPathInfo() + "\n" +
                "Data service path: " + dataServicePath + "\n" +
                "Metadata service path: " + metadataServicePath);
    }

    private String firstPathSegment(HttpServletRequest request) {
        String path = request.getPathInfo();

        if (path == null) {
            return "";
        }

        int indexOfNextSlash = path.indexOf("/", 1);

        if (indexOfNextSlash == -1) {
            return path;
        }

        return path.substring(0, indexOfNextSlash);
    }
}

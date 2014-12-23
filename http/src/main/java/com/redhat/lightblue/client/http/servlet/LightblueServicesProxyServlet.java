package com.redhat.lightblue.client.http.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class LightblueServicesProxyServlet extends PropertiesLightblueProxyServlet {
    /**
     * The first path segment to match in the incoming request to indicate it is a data service
     * request.
     */
    private String dataServicePath;

    /**
     * The first path segment to match in the incoming request to indicate it is a metadata service
     * request.
     */
    private String metadataServicePath;

    private String dataServiceUri;
    private String metadataServiceUri;

    @Override
    public void init() throws ServletException {
        super.init();

        dataServicePath = getInitParamWithDefault("dataServicePath", "/data").replaceAll("/+$", "");
        metadataServicePath = getInitParamWithDefault("metadataServicePath", "/metadata")
                .replaceAll("/+$", "");

        dataServiceUri = configuration().getDataServiceURI();
        metadataServiceUri= configuration().getMetadataServiceURI();

        if (dataServiceUri == null) {
            throw new LightblueServletException("No dataServiceURI defined in configuration, " +
                    configuration());
        }

        if (metadataServiceUri == null) {
            throw new LightblueServletException("No metadataServiceURI defined in configuration, " +
                    configuration());
        }

        // Get rid of trailing slashes.
        dataServiceUri = dataServiceUri.replaceAll("/+$", "");
        metadataServiceUri = metadataServiceUri.replaceAll("/+$", "");
    }

    @Override
    protected String serviceUriForRequest(HttpServletRequest request) throws ServletException {
        String firstPathSegment = firstPathSegment(request);

        if (firstPathSegment.equals(dataServicePath)) {
            return dataServiceUri + servicePathForRequest(request)
                    .substring(firstPathSegment.length());
        }

        if (firstPathSegment.equals(metadataServicePath)) {
            return metadataServiceUri + servicePathForRequest(request)
                    .substring(firstPathSegment.length());
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

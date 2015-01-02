package com.redhat.lightblue.client.http.servlet;

import com.redhat.lightblue.client.LightblueClientConfiguration;

import org.apache.http.impl.client.CloseableHttpClient;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public final class LightblueMetadataProxyServlet extends AbstractLightblueProxyServlet {
    private String metadataServiceUri;

    /**
     * @param httpClient The http client to use for this servlet. Servlets <em>should not</em>
     *         manage (e.g. close) the client; the client should manage its own lifecycle with
     *         regards to the container.
     */
    @Inject
    public LightblueMetadataProxyServlet(CloseableHttpClient httpClient,
            Instance<LightblueClientConfiguration> configuration) {
        super(httpClient, configuration);
    }

    @Override
    public void init() throws ServletException {
        LightblueClientConfiguration configuration = configuration();

        metadataServiceUri = getInitParamOrDefault("metadataServiceURI",
                configuration.getMetadataServiceURI());

        if (metadataServiceUri == null) {
            throw new LightblueServletException("No metadataServiceURI defined in configuration " +
                    "or in init parameter. Configuration checked was, " + configuration);
        }

        // Get rid of trailing slashes.
        metadataServiceUri = metadataServiceUri.replaceAll("/+$", "");
    }

    @Override
    protected String serviceUriForRequest(HttpServletRequest request) {
        return metadataServiceUri + servicePathForRequest(request);
    }
}

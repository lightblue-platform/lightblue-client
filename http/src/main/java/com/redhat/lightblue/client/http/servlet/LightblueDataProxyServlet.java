package com.redhat.lightblue.client.http.servlet;

import com.redhat.lightblue.client.LightblueClientConfiguration;

import org.apache.http.impl.client.CloseableHttpClient;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public final class LightblueDataProxyServlet extends AbstractLightblueProxyServlet {
    private String dataServiceUri;

    /**
     * @param httpClient The http client to use for this servlet. Servlets <em>should not</em>
     *         manage (e.g. close) the client; the client should manage its own lifecycle with
     *         regards to the container.
     */
    @Inject
    public LightblueDataProxyServlet(CloseableHttpClient httpClient,
            Instance<LightblueClientConfiguration> configuration) {
        super(httpClient, configuration);
    }

    @Override
    public void init() throws ServletException {
        LightblueClientConfiguration configuration = configuration();

        dataServiceUri = getInitParamOrDefault("dataServiceURI",
                configuration.getDataServiceURI());

        if (dataServiceUri == null) {
            throw new LightblueServletException("No dataServiceURI defined in configuration or in" +
                    " init parameter. Configuration checked was, " + configuration);
        }

        // Get rid of trailing slashes.
        dataServiceUri = dataServiceUri.replaceAll("/+$", "");
    }

    @Override
    protected String serviceUriForRequest(HttpServletRequest request) {
        return dataServiceUri + servicePathForRequest(request);
    }
}

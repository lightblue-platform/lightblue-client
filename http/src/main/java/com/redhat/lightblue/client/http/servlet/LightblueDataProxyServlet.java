package com.redhat.lightblue.client.http.servlet;

import com.redhat.lightblue.client.LightblueClientConfiguration;

import org.apache.http.impl.client.CloseableHttpClient;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * A Lightblue service proxy servlet, specifically for the data service, which
 * adds a {@code "dataServiceURI"} init parameter to define the data service URI
 * to forward requests to, or will fall back to the data service URI defines in
 * the [possibly injected]
 * {@link com.redhat.lightblue.client.LightblueClientConfiguration}.
 *
 * <p>
 * Example usage in web.xml:
 *
 * <pre><code>
 *   {@code<}servlet{@code>}
 *       {@code<}servlet-name>data-request{@code<}/servlet-name{@code>}
 *       {@code<}servlet-class>com.redhat.lightblue.client.http.servlet.LightblueDataProxyServlet{@code<}/servlet-class{@code>}
 *       {@code<}init-param{@code>}
 *           {@code<}param-name>dataServiceURI{@code<}/param-name{@code>}
 *           {@code<}param-value>http://lightblue.mycompany.com/data{@code<}/param-value{@code>}
 *        {@code<}/init-param{@code>}
 *   {@code<}/servlet{@code>}
 *   {@code<}servlet-mapping{@code>}
 *       {@code<}servlet-name>data-request{@code<}/servlet-name{@code>}
 *       {@code<}url-pattern>/rest-request/data/*{@code<}/url-pattern{@code>}
 *   {@code<}/servlet-mapping{@code>}
 * </code></pre>
 *
 * @see com.redhat.lightblue.client.http.servlet.AbstractLightblueProxyServlet
 */
public final class LightblueDataProxyServlet extends AbstractLightblueProxyServlet {
    private String dataServiceUri;

    /**
     * @see
     * AbstractLightblueProxyServlet#AbstractLightblueProxyServlet(CloseableHttpClient,
     * Instance)
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
            throw new LightblueServletException("No dataServiceURI defined in configuration or in"
                    + " init parameter. Configuration checked was, " + configuration);
        }

        // Get rid of trailing slashes.
        dataServiceUri = dataServiceUri.replaceAll("/+$", "");
    }

    @Override
    protected String serviceUriForRequest(HttpServletRequest request) {
        return dataServiceUri + servicePathForRequest(request);
    }
}

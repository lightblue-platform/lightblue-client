package com.redhat.lightblue.client.http.servlet;

import com.redhat.lightblue.client.LightblueClientConfiguration;

import org.apache.http.impl.client.CloseableHttpClient;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * A Lightblue service proxy servlet, specifically for the metadata service,
 * which adds a {@code "metadataServiceURI"} init parameter to define the data
 * service URI to forward requests to, or will fall back to the metadata service
 * URI defines in the [possibly injected]
 * {@link com.redhat.lightblue.client.LightblueClientConfiguration}.
 *
 * <p>
 * Example usage in web.xml:
 *
 * <pre><code>
 *   {@code<}servlet{@code>}
 *       {@code<}servlet-name>metadata-request{@code<}/servlet-name{@code>}
 *       {@code<}servlet-class>com.redhat.lightblue.client.http.servlet.LightblueMetadataProxyServlet{@code<}/servlet-class{@code>}
 *       {@code<}init-param{@code>}
 *           {@code<}param-name>metadataServiceURI{@code<}/param-name{@code>}
 *           {@code<}param-value>http://lightblue.mycompany.com/metadata{@code<}/param-value{@code>}
 *        {@code<}/init-param{@code>}
 *   {@code<}/servlet{@code>}
 *   {@code<}servlet-mapping{@code>}
 *       {@code<}servlet-name>metadata-request{@code<}/servlet-name{@code>}
 *       {@code<}url-pattern>/rest-request/metadata/*{@code<}/url-pattern{@code>}
 *   {@code<}/servlet-mapping{@code>}
 * </code></pre>
 *
 * @see com.redhat.lightblue.client.http.servlet.AbstractLightblueProxyServlet
 */
public final class LightblueMetadataProxyServlet extends AbstractLightblueProxyServlet {
    private String metadataServiceUri;

    /**
     * @see
     * AbstractLightblueProxyServlet#AbstractLightblueProxyServlet(CloseableHttpClient,
     * Instance)
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
            throw new LightblueServletException("No metadataServiceURI defined in configuration "
                    + "or in init parameter. Configuration checked was, " + configuration);
        }

        // Get rid of trailing slashes.
        metadataServiceUri = metadataServiceUri.replaceAll("/+$", "");
    }

    @Override
    protected String serviceUriForRequest(HttpServletRequest request) {
        return metadataServiceUri + servicePathForRequest(request);
    }
}

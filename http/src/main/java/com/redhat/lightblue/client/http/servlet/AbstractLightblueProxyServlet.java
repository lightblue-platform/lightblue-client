package com.redhat.lightblue.client.http.servlet;

import com.redhat.lightblue.client.LightblueClientConfiguration;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * An injectable Lightblue proxy servlet, powered by Apache
 * {@link org.apache.http.impl.client.CloseableHttpClient}. See
 * {@link AbstractLightblueProxyServlet#AbstractLightblueProxyServlet(CloseableHttpClient, Instance)}
 * for how to setup the injection.
 */
public abstract class AbstractLightblueProxyServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLightblueProxyServlet.class);

    /**
     * This is okay to share because of isRepeatable. Just don't go modifying the headers or
     * something willy nilly.
     */
    private static final HttpEntity ERROR_RESPONSE = new StringEntity(
            "{\"error\":\"There was a problem calling the lightblue service\"}",
            ContentType.APPLICATION_JSON);

    private final CloseableHttpClient httpClient;
    private final Instance<LightblueClientConfiguration> configuration;

    /**
     * An {@link javax.inject.Inject @Inject}able constructor for the servlet. To setup the dependency for the servlet,
     * you will have to use Java CDI, providing a {@link javax.enterprise.inject.Produces}-annotated
     * method whch instantiates an appropriately configured
     * {@link org.apache.http.impl.client.CloseableHttpClient} to use inside the servlets, with a
     * preferred scope (ideally {@link javax.enterprise.context.ApplicationScoped}). To shutdown
     * the servlet, a "disposer" must also be provided using the
     * {@link javax.enterprise.inject.Disposes} annotation.
     *
     * <p>For convenience, lightblue-client provides factory methods for default http clients with
     * basic configuration. Of course, you are free to configure the client however you need.
     *
     * <p>An instance of {@link com.redhat.lightblue.client.LightblueClientConfiguration} may also
     * be injected here, but it is optional. By default, this servlet will use the configuration
     * defined via
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration#fromDefault()}.
     *
     * <p>Example producer and disposer:
     * 
     * <pre><code>
     * public class ApplicationContext {
     *     {@literal@}Produces {@literal@}ApplicationScoped
     *     public LightblueClientConfiguration getLightblueClientConfiguration() {
     *         return PropertiesLightblueClientConfiguration.fromDefault();
     *     }
     *
     *     {@literal@}Produces {@literal@}ApplicationScoped
     *     public CloseableHttpClient getClient(LightblueClientConfiguration config) throws Exception {
     *         return ApacheHttpClients.fromLightblueClientConfiguration(config);
     *     }
     *
     *     public void closeHttpClient({@literal@}Disposes CloseableHttpClient client) throws IOException {
     *         client.close();
     *     }
     * }
     * </code></pre>
     * @param httpClient The http client to use for this servlet. Servlets <em>should not</em>
     *         manage (e.g. close) the client; the client should manage its own lifecycle with
     *         regards to the container.
     *
     * @see <a href="http://docs.oracle.com/javaee/6/tutorial/doc/giwhl.html">Overview of CDI &mdash; The Java EE 6 Tutorial</a>
     */
    @Inject
    public AbstractLightblueProxyServlet(CloseableHttpClient httpClient,
            Instance<LightblueClientConfiguration> configuration) {
        this.httpClient = httpClient;
        this.configuration = configuration;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException {
        res.setContentType("application/json");

        try {
            HttpUriRequest apacheHttpRequest = proxyRequest(req);

            try (CloseableHttpResponse httpResponse = httpClient.execute(apacheHttpRequest)) {
                HttpEntity entity = httpResponse.getEntity();
                entity.writeTo(res.getOutputStream());
            }
        } catch (IOException | ServletException e) {
            ERROR_RESPONSE.writeTo(res.getOutputStream());
            LOGGER.error("There was a problem calling the lightblue service", e);
        }
    }

    /**
     * Constructs a service URI from the given request. The request will have a path for the
     * servlet, which will contain information we need to pass on to the service. For convenience,
     * see {@link #servicePathForRequest(javax.servlet.http.HttpServletRequest)}.
     *
     * @throws javax.servlet.ServletException
     */
    protected abstract String serviceUriForRequest(HttpServletRequest request) throws ServletException;

    protected LightblueClientConfiguration configuration() {
        if (configuration.isUnsatisfied()) {
            return defaultConfiguration();
        }

        return configuration.get();
    }

    protected LightblueClientConfiguration defaultConfiguration() {
        return new LightblueServletContextConfiguration(getServletContext())
                .lightblueClientConfiguration();
    }

    /**
     * @return The url for the request with the context and the servlet path stripped out, which
     * leaves whatever wild cards were left that matched this servlet.
     *
     * <p>For example:
     * <ul>
     *     <li>Given the request, "http://my.site.com/app/get/the/thing?foo=bar"</li>
     *     <li>and some servlet which maps to, "/get/*"</li>
     *     <li>in an application with context, "/app"</li>
     *     <li>then this method would return, "/the/thing?foo=bar"</li>
     * </ul>
     */
    protected final String servicePathForRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        String queryString = request.getQueryString();

        String path = "";

        if (pathInfo != null) {
            path += pathInfo;
        }

        if (queryString != null) {
            path += "?" + queryString;
        }

        return path;
    }

    protected final String getInitParamOrDefault(String key, String defaultValue) {
        String value = getInitParameter(key);
        return value == null ? defaultValue : value;
    }

    /**
     * For the given servlet request, return a new request object (for use with Apache HttpClient),
     * that may be executed in place of the original request to make the real service call.
     *
     * @throws javax.servlet.ServletException
     */
    private HttpUriRequest proxyRequest(HttpServletRequest request) throws ServletException,
            IOException {
        String newUri = serviceUriForRequest(request);

        BasicHttpEntityEnclosingRequest httpRequest =
                new BasicHttpEntityEnclosingRequest(request.getMethod(), newUri);
        HttpEntity entity = new InputStreamEntity(request.getInputStream(),
                request.getContentLength());
        httpRequest.setEntity(entity);

        try {
            // Sadly have to do this to set the URI; it is not derived from the original httpRequest
            HttpRequestWrapper wrappedRequest = HttpRequestWrapper.wrap(httpRequest);
            wrappedRequest.setURI(new URI(newUri));

            return wrappedRequest;
        } catch (URISyntaxException e) {
            LOGGER.error("Syntax exception in service URI, " + newUri, e);
            throw new LightblueServletException("Syntax exception in service URI, " + newUri, e);
        }
    }
}

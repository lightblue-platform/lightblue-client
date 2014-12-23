package com.redhat.lightblue.client.http.servlet;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.auth.HttpClientAuth;
import com.redhat.lightblue.client.http.auth.HttpClientCertAuth;
import com.redhat.lightblue.client.http.auth.HttpClientNoAuth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractLightblueProxyServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLightblueProxyServlet.class);

    /**
     * This is okay to share because of isRepeatable. Just don't go modifying the headers or
     * something willy nilly.
     */
    private static final HttpEntity ERROR_RESPONSE = new StringEntity(
            "{\"error\":\"There was a problem calling the lightblue service\"}",
            ContentType.APPLICATION_JSON);

    /** @see #init() */
    private LightblueClientConfiguration configuration;

    /** @see #init() */
    private CloseableHttpClient httpClient;

    /**
     * Accepts parameters for the connection pool used for the http client. For example:
     *
     * <pre><code>
     *     {@code<servlet>}
     *         {@code<servlet-name>}lightblueDataService{@code</servlet-name>}
     *         {@code<servlet-class>}com.redhat.lightblue.client.http.servlet.LightblueDataProxyServlet{@code</servlet-class>}
     *         {@code<init-param>}
     *             {@code<param-name>}maxConnectionsPerRoute{@code</param-name>}
     *             {@code<param-value>}10{@code</param-value>}
     *         {@code</init-param>}
     *         {@code<init-param>}
     *             {@code<param-name>}maxConnectionsTotal{@code</param-name>}
     *             {@code<param-value>}50{@code</param-value>}
     *         {@code</init-param>}
     *     {@code</servlet>}
     * </code></pre>
     *
     * By default, the maxConnectionsPerRoute is 5, and maxConnectionsTotal is 30.
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        configuration = configuration();

        int maxPerRoute = Integer.parseInt(getInitParamWithDefault("maxConnectionsPerRoute", "5"));
        int maxTotal = Integer.parseInt(getInitParamWithDefault("maxConnectionsTotal", "30"));

        httpClient = getLightblueHttpClient(maxPerRoute, maxTotal);
    }

    @Override
    public void destroy() {
        try {
            httpClient.close();
        } catch (IOException e) {
            LOGGER.debug("Error closing http client.", e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException {
        HttpEntity entity;

        try {
            HttpUriRequest apacheHttpRequest = proxyRequest(req);

            try (CloseableHttpResponse httpResponse = httpClient.execute(apacheHttpRequest)) {
                entity = httpResponse.getEntity();
            }
        } catch (IOException | ServletException e) {
            entity = ERROR_RESPONSE;
            LOGGER.error("There was a problem calling the lightblue service", e);
        }

        res.setContentType("application/json");
        entity.writeTo(res.getOutputStream());
    }

    /**
     * The configuration is used to determine the authentication details.
     * @see #getSocketFactory()
     */
    protected abstract LightblueClientConfiguration configuration();

    /**
     * Constructs a service URI from the given request. The request will have a path for the
     * servlet, which will contain information we need to pass on to the service. For convenience,
     * see {@link #servicePathForRequest(javax.servlet.http.HttpServletRequest)}.
     *
     * @throws javax.servlet.ServletException
     */
    protected abstract String serviceUriForRequest(HttpServletRequest request) throws ServletException;

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

    protected final String getInitParamWithDefault(String key, String defaultValue) {
        String value = getInitParameter(key);
        return value == null ? defaultValue : value;
    }

    /**
     * For the given servlet request, return a new request object (for use with Apache HttpClient),
     * that may be executed in place of the original request to make the real service call.
     *
     * @throws javax.servlet.ServletException
     */
    private HttpUriRequest proxyRequest(HttpServletRequest request) throws ServletException {
        String newUri = serviceUriForRequest(request);

        if (newUri == null) {
            return null;
        }

        HttpRequest apacheHttpRequest = new BasicHttpRequest(request.getMethod(), newUri);
        return HttpRequestWrapper.wrap(apacheHttpRequest);
    }

    /**
     * @param maxPerRoute The amount of concurrent http connections per route.
     * @param maxTotal The amount of total concurrent http connections.
     *
     * @see org.apache.http.impl.conn.PoolingHttpClientConnectionManager
     * @see org.apache.http.pool.ConnPoolControl
     */
    private CloseableHttpClient getLightblueHttpClient(int maxPerRoute, int maxTotal) {
        SSLConnectionSocketFactory sslSocketFactory = getSocketFactory();
        Registry<ConnectionSocketFactory> socketFactoryRegistry;
        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        connectionManager.setMaxTotal(maxTotal);

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                // I don't think this is necessary?
                .setSSLSocketFactory(sslSocketFactory)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }

    /**
     * @return An SSL socket factory to use based on whether or not {@link #configuration}
     * .useCertAuth() is true.
     */
    private SSLConnectionSocketFactory getSocketFactory() {
        HttpClientAuth clientAuth;

        if(configuration.useCertAuth()) {
            LOGGER.debug("Using certificate authentication");
            clientAuth = new HttpClientCertAuth(configuration);
        } else {
            LOGGER.debug("Using no authentication");
            clientAuth = new HttpClientNoAuth();
        }

        return clientAuth.getSSLConnectionSocketFactory();
    }
}

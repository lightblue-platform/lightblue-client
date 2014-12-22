package com.redhat.lightblue.client.http;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.auth.HttpClientCertAuth;
import com.redhat.lightblue.client.http.auth.HttpClientNoAuth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractLightblueProxyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLightblueProxyServlet.class);

    /** @see #init() */
    protected LightblueClientConfiguration configuration;

    /** @see #init() */
    private String serviceUri;

    @Override
    public void init() throws ServletException {
        configuration = new PropertiesLightblueClientConfiguration();
        serviceUri = serviceUri();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        try {
            try (CloseableHttpClient httpClient = getLightblueHttpClient()) {
                HttpUriRequest apacheHttpRequest = proxyRequest(req);

                try (CloseableHttpResponse httpResponse = httpClient.execute(apacheHttpRequest)) {
                    HttpEntity entity = httpResponse.getEntity();
                    entity.writeTo(res.getOutputStream());
                }
            }
        } catch (RuntimeException e) {
            res.getWriter().println("{\"error\":\"There was a problem calling the lightblue service\"}");
            LOGGER.error("There was a problem calling the lightblue service", e);
        }
    }

    /**
     * @return The base service URI to use (data or metadata). Should <em>not</em> end with a /
     * character.
     */
    protected abstract String serviceUri();

    /**
     * For the given servlet request, return a new request object (for use with Apache HttpClient),
     * that may be executed in place of the original request to make the real service call.
     */
    private HttpUriRequest proxyRequest(HttpServletRequest request) {
        String newUri = serviceUriForRequest(request);
        HttpRequest apacheHttpRequest = new BasicHttpRequest(request.getMethod(), newUri);
        return HttpRequestWrapper.wrap(apacheHttpRequest);
    }

    /**
     * Constructs a service URI from the given request. The request will have a path for the
     * servlet, which will contain information we need to pass on to the service. So, the service
     * URI for the request is constructed from the base service URI ({@link #serviceUri()}) and
     * the path and query string information from the request.
     */
    private String serviceUriForRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        String queryString = request.getQueryString();

        //
        String serviceUri = this.serviceUri;

        if (pathInfo != null) {
            serviceUri += pathInfo;
        }

        if (queryString != null) {
            serviceUri += "?" + queryString;
        }

        return serviceUri;
    }

    private CloseableHttpClient getLightblueHttpClient() {
        if(configuration.useCertAuth()) {
            LOGGER.debug("Using certificate authentication");
            return new HttpClientCertAuth(configuration).getClient();
        } else {
            LOGGER.debug("Using no authentication");
            return new HttpClientNoAuth().getClient();
        }
    }

    private String ensureStartingSlashAndNoTrailingSlash(String path) {
        return "/" + path.replaceAll("^/+", "").replaceAll("/+$", "");
    }
}

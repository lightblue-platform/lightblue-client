package com.redhat.lightblue.client.http.servlet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.testing.doubles.FakeServletConfig;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RunWith(JUnit4.class)
public class LightblueServicesProxyServletTest {
    /**
     * Must call init() before using in each test, after setting up a
     * {@link javax.servlet.ServletConfig}. Use
     * {@link com.redhat.lightblue.client.http.testing.doubles.FakeServletConfig} to do this.
     */
    private LightblueServicesProxyServlet servlet;
    private LightblueClientConfiguration config;

    /**
     * This is kind of hacky.
     * TODO: Make servlet more testable (inject dependencies)
     */
    @Before
    public void createTestableServlet() {
        config = new LightblueClientConfiguration();
        servlet = new LightblueServicesProxyServlet() {
            // Override getLightblueHttpClient to avoid making a real http client...
            // really this should be an injected dependency.
            @Override
            CloseableHttpClient getLightblueHttpClient(int maxPerRoute, int maxTotal) {
                return null;
            }

            // Override so we can control config scheme
            @Override
            protected LightblueClientConfiguration configuration() {
                return config;
            }
        };
    }

    @Test
    public void shouldConstuctProxyUrlsForDataRequestsStartingWithSlashDataByDefault()
            throws ServletException {
        config.setDataServiceURI("http://url.to.data/service");
        config.setMetadataServiceURI("http://url.to.metadata/service");

        servlet.init(new FakeServletConfig());

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/data/find/myentity/1.0.0");

        assertEquals("http://url.to.data/service/find/myentity/1.0.0",
                servlet.serviceUriForRequest(request));
    }

    @Test
    public void shouldConstuctProxyUrlsForDataRequests() throws ServletException {
        config.setDataServiceURI("http://url.to.data/service");
        config.setMetadataServiceURI("http://url.to.metadata/service");

        servlet.init(new FakeServletConfig()
                .setInitParameter("dataServicePath", "/alt-data-path"));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/alt-data-path/find/myentity/1.0.0");

        assertEquals("http://url.to.data/service/find/myentity/1.0.0",
                servlet.serviceUriForRequest(request));
    }

    @Test
    public void shouldConstuctProxyUrlsForMetadataRequestsStartingWithSlashMetadataByDefault()
            throws ServletException {
        config.setDataServiceURI("http://url.to.data/service");
        config.setMetadataServiceURI("http://url.to.metadata/service");

        servlet.init(new FakeServletConfig());

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/metadata/myentity/1.0.0");

        assertEquals("http://url.to.metadata/service/myentity/1.0.0",
                servlet.serviceUriForRequest(request));
    }

    @Test
    public void shouldConstuctProxyUrlsForMetadataRequests() throws ServletException {
        config.setDataServiceURI("http://url.to.data/service");
        config.setMetadataServiceURI("http://url.to.metadata/service");

        servlet.init(new FakeServletConfig()
                .setInitParameter("metadataServicePath", "/alt-metadata-path"));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/alt-metadata-path/myentity/1.0.0");

        assertEquals("http://url.to.metadata/service/myentity/1.0.0",
                servlet.serviceUriForRequest(request));
    }

    @Test
    public void shouldStripTrailingSlashesInDataServiceUri() throws ServletException {
        config.setDataServiceURI("http://url.to.data/service/");
        config.setMetadataServiceURI("http://url.to.metadata/service");

        servlet.init(new FakeServletConfig());

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/data/find/myentity/1.0.0");

        assertEquals("http://url.to.data/service/find/myentity/1.0.0",
                servlet.serviceUriForRequest(request));
    }

    @Test
    public void shouldStripTrailingSlashesInMetadataServiceUri() throws ServletException {
        config.setDataServiceURI("http://url.to.data/service");
        config.setMetadataServiceURI("http://url.to.metadata/service/");

        servlet.init(new FakeServletConfig());

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/metadata/myentity/1.0.0");

        assertEquals("http://url.to.metadata/service/myentity/1.0.0",
                servlet.serviceUriForRequest(request));
    }
}

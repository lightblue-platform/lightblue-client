package com.redhat.lightblue.client.http.servlet;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.testing.doubles.FakeServletConfig;
import com.redhat.lightblue.client.http.testing.doubles.StubHttpServletRequest;
import com.redhat.lightblue.client.http.testing.doubles.StubInstance;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LightblueMetadataProxyServletTest {
    @Test
    public void shouldUseMetadataServiceUriFromConfiguration() throws MalformedURLException, ServletException {
        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setMetadataServiceURI("http://service.com/metadata");

        LightblueMetadataProxyServlet servlet = new LightblueMetadataProxyServlet(
                mock(CloseableHttpClient.class),
                new StubInstance<>(config));

        servlet.init(new FakeServletConfig());

        HttpServletRequest request = new StubHttpServletRequest("http://myapp.com/metadata/thing",
                "POST", "{\"test\":0}", "application/json", "/metadata/*");

        assertEquals("http://service.com/metadata/thing", servlet.serviceUriForRequest(request));
    }

    @Test
    public void shouldUseMetadataServiceUriFromServletInitParamOverConfiguration() throws ServletException,
            MalformedURLException {
        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setDataServiceURI("http://not.the.right.url/metadata");

        LightblueMetadataProxyServlet servlet = new LightblueMetadataProxyServlet(
                mock(CloseableHttpClient.class),
                new StubInstance<>(config));

        servlet.init(new FakeServletConfig()
                .setInitParameter("metadataServiceURI", "http://myservice.com/metadata"));

        HttpServletRequest request = new StubHttpServletRequest("http://myapp.com/metadata/thing",
                "POST", "{\"test\":0}", "application/json", "/metadata/*");

        assertEquals("http://myservice.com/metadata/thing", servlet.serviceUriForRequest(request));
    }

    @Test
    public void shouldNotBlowUpIfMetadataServiceUriHasTrailingSlash() throws ServletException,
            MalformedURLException {
        LightblueClientConfiguration config = new LightblueClientConfiguration();
        LightblueMetadataProxyServlet servlet = new LightblueMetadataProxyServlet(
                mock(CloseableHttpClient.class),
                new StubInstance<>(config));

        servlet.init(new FakeServletConfig()
                .setInitParameter("metadataServiceURI", "http://myservice.com/metadata/"));

        HttpServletRequest request = new StubHttpServletRequest("http://myapp.com/metadata/thing",
                "POST", "{\"test\":0}", "application/json", "/metadata/*");

        assertEquals("http://myservice.com/metadata/thing", servlet.serviceUriForRequest(request));
    }
}

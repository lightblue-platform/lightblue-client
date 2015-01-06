package com.redhat.lightblue.client.http.servlet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.testing.doubles.FakeServletConfig;
import com.redhat.lightblue.client.http.testing.doubles.StubHttpServletRequest;
import com.redhat.lightblue.client.http.testing.doubles.StubInstance;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

@RunWith(JUnit4.class)
public class LightblueDataProxyServletTest {
    @Test
    public void shouldUseDataServiceUriFromConfiguration() throws MalformedURLException, ServletException {
        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setDataServiceURI("http://service.com/data");

        LightblueDataProxyServlet servlet = new LightblueDataProxyServlet(
                mock(CloseableHttpClient.class),
                new StubInstance<>(config));

        servlet.init(new FakeServletConfig());

        HttpServletRequest request = new StubHttpServletRequest("http://myapp.com/data/find/thing",
                "POST", "{\"test\":0}", "application/json", "/data/*");

        assertEquals("http://service.com/data/find/thing", servlet.serviceUriForRequest(request));
    }

    @Test
    public void shouldUseDataServiceUriFromServletInitParamOverConfiguration() throws ServletException,
            MalformedURLException {
        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setDataServiceURI("http://not.the.right.url/data");

        LightblueDataProxyServlet servlet = new LightblueDataProxyServlet(
                mock(CloseableHttpClient.class),
                new StubInstance<>(config));

        servlet.init(new FakeServletConfig()
                .setInitParameter("dataServiceURI", "http://myservice.com/data"));

        HttpServletRequest request = new StubHttpServletRequest("http://myapp.com/data/find/thing",
                "POST", "{\"test\":0}", "application/json", "/data/*");

        assertEquals("http://myservice.com/data/find/thing", servlet.serviceUriForRequest(request));
    }

    @Test
    public void shouldNotBlowUpIfDataServiceUriHasTrailingSlash() throws ServletException,
            MalformedURLException {
        LightblueClientConfiguration config = new LightblueClientConfiguration();
        LightblueDataProxyServlet servlet = new LightblueDataProxyServlet(
                mock(CloseableHttpClient.class),
                new StubInstance<>(config));

        servlet.init(new FakeServletConfig()
                .setInitParameter("dataServiceURI", "http://myservice.com/data/"));

        HttpServletRequest request = new StubHttpServletRequest("http://myapp.com/data/find/thing",
                "POST", "{\"test\":0}", "application/json", "/data/*");

        assertEquals("http://myservice.com/data/find/thing", servlet.serviceUriForRequest(request));
    }
}

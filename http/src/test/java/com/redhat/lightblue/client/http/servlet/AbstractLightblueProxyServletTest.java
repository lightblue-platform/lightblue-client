package com.redhat.lightblue.client.http.servlet;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.testing.doubles.FakeServletOutputStream;
import com.redhat.lightblue.client.http.testing.doubles.StubHttpServletRequest;
import com.redhat.lightblue.client.http.testing.doubles.StubInstance;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.enterprise.inject.Instance;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

@RunWith(JUnit4.class)
public class AbstractLightblueProxyServletTest {
    @Test
    public void shouldAlwaysRespondWithJsonType() throws ServletException, IOException {
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class, Mockito.RETURNS_DEEP_STUBS);

        AbstractLightblueProxyServlet servlet = getTestServlet(mockHttpClient, null, null, null);

        HttpServletRequest stubRequest = new StubHttpServletRequest("http://test.com/servlet/file",
                "GET", "{test:0}", "application/json", "/servlet/*");

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ServletOutputStream outputStream = new FakeServletOutputStream(new ByteArrayOutputStream());
        when(mockResponse.getOutputStream()).thenReturn(outputStream);

        servlet.service(stubRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
    }

    @Test
    public void shouldReturnTheServicePathForTheRequest() throws ServletException, IOException {
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class, Mockito.RETURNS_DEEP_STUBS);

        AbstractLightblueProxyServlet servlet = getTestServlet(mockHttpClient, null, null, null);

        HttpServletRequest stubRequest = new StubHttpServletRequest("http://my.site.com/app/get/the/thing?foo=bar",
                "GET", "{test:0}", "application/json", "/get/*");

        assertEquals("/the/thing?foo=bar", servlet.servicePathForRequest(stubRequest));
    }

    @Test
    public void shouldProxyTheRequest() throws ServletException, IOException, URISyntaxException {
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class, Mockito.RETURNS_DEEP_STUBS);

        AbstractLightblueProxyServlet servlet = getTestServlet(mockHttpClient, null, "http://myservice.com", null);

        HttpServletRequest stubRequest = new StubHttpServletRequest("http://my.site.com/app/get/the/thing?foo=bar",
                "GET", "{test:0}", "application/json", "/get/*");

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ServletOutputStream outputStream = new FakeServletOutputStream(new ByteArrayOutputStream());
        when(mockResponse.getOutputStream()).thenReturn(outputStream);

        servlet.service(stubRequest, mockResponse);

        ArgumentCaptor<HttpUriRequest> requestCaptor = ArgumentCaptor.forClass(HttpUriRequest.class);

        verify(mockHttpClient).execute(requestCaptor.capture());

        HttpUriRequest request = requestCaptor.getValue();

        assertEquals("GET", request.getMethod());
        assertEquals(new URI("http://myservice.com/the/thing?foo=bar"), request.getURI());
        assertThat(request, instanceOf(HttpEntityEnclosingRequest.class));

        HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest) request;
        ByteArrayOutputStream entityOutStream = new ByteArrayOutputStream();
        entityEnclosingRequest.getEntity().writeTo(entityOutStream);

        byte[] expectedEntityBytes = new byte[stubRequest.getContentLength()];
        stubRequest.getInputStream().read(expectedEntityBytes, 0, stubRequest.getContentLength());

        assertEquals(new String(expectedEntityBytes), entityOutStream.toString());
    }

    private AbstractLightblueProxyServlet getTestServlet(CloseableHttpClient httpClient,
            LightblueClientConfiguration clientconfig, final String serviceUri,
            ServletConfig servletConfig) throws ServletException {
        Instance<LightblueClientConfiguration> instance = new StubInstance<>(clientconfig);

        AbstractLightblueProxyServlet servlet;
        servlet = new AbstractLightblueProxyServlet(httpClient, instance) {
            @Override
            protected String serviceUriForRequest(HttpServletRequest request) throws ServletException {
                return serviceUri + servicePathForRequest(request);
            };
        };

        if (servletConfig == null) {
            servlet.init();
        } else {
            servlet.init(servletConfig);
        }

        return servlet;
    }
}

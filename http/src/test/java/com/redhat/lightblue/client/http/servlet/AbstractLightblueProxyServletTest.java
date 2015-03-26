/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.http.servlet;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.testing.doubles.FakeServletConfig;
import com.redhat.lightblue.client.http.testing.doubles.FakeServletOutputStream;
import com.redhat.lightblue.client.http.testing.doubles.StubHttpServletRequest;
import com.redhat.lightblue.client.http.testing.doubles.StubInstance;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import javax.enterprise.inject.Instance;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
    public void shouldProxyTheRequestMethodUriAndBody() throws ServletException, IOException, URISyntaxException {
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

    @Test
    public void shouldUseInjectedClientConfigurationIfSatisfied() throws ServletException {
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class, Mockito.RETURNS_DEEP_STUBS);
        LightblueClientConfiguration config = new LightblueClientConfiguration();

        AbstractLightblueProxyServlet servlet = getTestServlet(mockHttpClient, config, null, null);

        assertSame(config, servlet.configuration());
    }

    @Test
    public void shouldDefaultToDefaultPropertiesFileForClientConfigIfNoneIsInjected() throws ServletException {
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class, Mockito.RETURNS_DEEP_STUBS);

        ServletConfig servletConfig = new FakeServletConfig()
                .setServletContext(mock(ServletContext.class));
        AbstractLightblueProxyServlet servlet = getTestServlet(mockHttpClient, null, null, servletConfig);

        LightblueClientConfiguration expected = PropertiesLightblueClientConfiguration.fromDefault();

        assertEquals(expected, servlet.configuration());
    }

    @Test
    public void shouldRespondWithErrorWhenBadServiceUri() throws ServletException, IOException {
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class, Mockito.RETURNS_DEEP_STUBS);

        AbstractLightblueProxyServlet servlet = getTestServlet(mockHttpClient, null, "bad service uri",
                null);

        HttpServletRequest stubRequest = new StubHttpServletRequest("http://my.site.com/app",
                "GET", "{test:0}", "application/json", "/app/*");

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ServletOutputStream outputStream = new FakeServletOutputStream(new ByteArrayOutputStream());
        when(mockResponse.getOutputStream()).thenReturn(outputStream);

        servlet.service(stubRequest, mockResponse);

        String expectedError = "{\"error\":\"There was a problem calling the lightblue service\"}";

        assertEquals(expectedError, mockResponse.getOutputStream().toString());
    }

    @Test
    public void shouldRespondWithErrorWhenProxyRequestFails() throws ServletException, IOException {
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class, Mockito.RETURNS_DEEP_STUBS);
        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenThrow(IOException.class);

        AbstractLightblueProxyServlet servlet = getTestServlet(mockHttpClient, null,
                "http://myservice.com", null);

        HttpServletRequest stubRequest = new StubHttpServletRequest("http://my.site.com/app",
                "GET", "{test:0}", "application/json", "/app/*");

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ServletOutputStream outputStream = new FakeServletOutputStream(new ByteArrayOutputStream());
        when(mockResponse.getOutputStream()).thenReturn(outputStream);

        servlet.service(stubRequest, mockResponse);

        String expectedError = "{\"error\":\"There was a problem calling the lightblue service\"}";

        assertEquals(expectedError, mockResponse.getOutputStream().toString());
    }

    @Test
    public void shouldRespondWithLightblueServiceResponseBodyThenCloseResponse() throws IOException, ServletException {
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse mockLightblueResponse = mock(CloseableHttpResponse.class);
        HttpEntity mockEntity = mock(HttpEntity.class);

        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenReturn(mockLightblueResponse);
        when(mockLightblueResponse.getEntity()).thenReturn(mockEntity);

        AbstractLightblueProxyServlet servlet = getTestServlet(mockHttpClient, null,
                "http://myservice.com", null);

        HttpServletRequest stubRequest = new StubHttpServletRequest("http://test.com/servlet/file",
                "GET", "{test:0}", "application/json", "/servlet/*");

        HttpServletResponse mockProxyResponse = mock(HttpServletResponse.class);
        ServletOutputStream outputStream = new FakeServletOutputStream(new ByteArrayOutputStream());
        when(mockProxyResponse.getOutputStream()).thenReturn(outputStream);

        servlet.service(stubRequest, mockProxyResponse);

        InOrder inOrder = inOrder(mockEntity, mockLightblueResponse);
        inOrder.verify(mockEntity).writeTo(outputStream);
        inOrder.verify(mockLightblueResponse).close();
    }

    @Test
    public void shouldGetServletInitParamOrDefault() throws ServletException {
        ServletConfig fakeServletConfig = new FakeServletConfig()
                .setInitParameter("testParam", "testValue");
        AbstractLightblueProxyServlet servlet = getTestServlet(mock(CloseableHttpClient.class),
                null, null, fakeServletConfig);

        assertEquals("testValue", servlet.getInitParamOrDefault("testParam", "defaultValue"));
        assertEquals("testDefaultValue", servlet.getInitParamOrDefault("nullParam", "testDefaultValue"));
    }

    protected AbstractLightblueProxyServlet getTestServlet(CloseableHttpClient httpClient,
            LightblueClientConfiguration clientConfig, final String serviceUri,
            ServletConfig servletConfig) throws ServletException {
        Instance<LightblueClientConfiguration> instance = new StubInstance<>(clientConfig);

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

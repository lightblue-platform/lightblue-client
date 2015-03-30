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

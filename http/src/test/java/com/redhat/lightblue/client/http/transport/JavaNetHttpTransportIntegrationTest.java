package com.redhat.lightblue.client.http.transport;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.http.LightblueHttpClientException;
import com.redhat.lightblue.client.http.testing.doubles.FakeLightblueRequest;
import com.redhat.lightblue.client.request.LightblueRequest;

/**
 * Tests the somewhat complicated and vaguely documented semantics of JDK's
 * {@link java.net.HttpURLConnection} are followed correctly by use of a real HTTP server with real
 * real connections.
 *
 * @see JavaNetHttpTransportTest
 */
@RunWith(JUnit4.class)
public class JavaNetHttpTransportIntegrationTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    private JavaNetHttpTransport client = new JavaNetHttpTransport();

    @Test
    public void shouldReturnResponseBodyOfSuccessfulRequest() throws LightblueHttpClientException {
        wireMockRule.stubFor(any(urlMatching(".*"))
                .willReturn(aResponse().withBody("The body").withStatus(200)));

        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/");

        String response = client.executeRequest(request, wireMockUrl());

        assertThat(response, is("The body"));
    }

    @Test
    public void shouldReturnResponseBodyOfUnsuccessfulRequest() throws LightblueHttpClientException {
        wireMockRule.stubFor(any(urlMatching(".*"))
                .willReturn(aResponse().withBody("The body").withStatus(500)));

        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/");


        Assert.assertEquals("The body", client.executeRequest(request, wireMockUrl()));
    }

    @Test
    public void shouldThrowExceptionOnUnsuccessfulRequestWithNoResponseBody() {
        wireMockRule.stubFor(any(urlMatching(".*"))
                .willReturn(aResponse().withStatus(500)));

        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/");

        try {
            client.executeRequest(request, wireMockUrl());
            Assert.fail();
        } catch (LightblueHttpClientException e) {
            Assert.assertEquals(500, e.getHttpStatus());
            assertThat(e.getHttpResponseBody(), is(""));
        }
    }

    private String wireMockUrl() {
        return "http://localhost:" + wireMockRule.port();
    }
}

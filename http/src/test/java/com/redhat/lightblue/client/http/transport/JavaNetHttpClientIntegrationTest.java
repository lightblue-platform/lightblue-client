package com.redhat.lightblue.client.http.transport;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.http.testing.doubles.FakeLightblueRequest;
import com.redhat.lightblue.client.request.LightblueRequest;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

/**
 * Tests the somewhat complicated and vaguely documented semantics of JDK's
 * {@link java.net.HttpURLConnection} are followed correctly by use of a real HTTP server with real
 * real connections.
 *
 * @see JavaNetHttpClientTest
 */
@RunWith(JUnit4.class)
public class JavaNetHttpClientIntegrationTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private JavaNetHttpClient client = new JavaNetHttpClient();

    @Test
    public void shouldReturnResponseBodyOfSuccessfulRequest() throws IOException {
        wireMockRule.stubFor(any(urlMatching(".*"))
                .willReturn(aResponse().withBody("The body").withStatus(200)));

        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/");

        String response = client.executeRequest(request, wireMockUrl());

        assertThat(response, is("The body"));
    }

    @Test
    public void shouldReturnResponseBodyOfUnsuccessfulRequest() throws IOException {
        wireMockRule.stubFor(any(urlMatching(".*"))
                .willReturn(aResponse().withBody("The body").withStatus(500)));

        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/");

        String response = client.executeRequest(request, wireMockUrl());

        assertThat(response, is("The body"));
    }

    @Test
    public void shouldReturnEmptyStringOfUnsuccessfulRequestWithNoResponseBody() throws IOException {
        wireMockRule.stubFor(any(urlMatching(".*"))
                .willReturn(aResponse().withStatus(500)));

        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/");

        String response = client.executeRequest(request, wireMockUrl());

        assertThat(response, is(""));
    }

    private String wireMockUrl() {
        return "http://localhost:" + wireMockRule.port();
    }
}

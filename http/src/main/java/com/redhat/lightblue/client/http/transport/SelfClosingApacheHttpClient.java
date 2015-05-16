package com.redhat.lightblue.client.http.transport;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.request.LightblueRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * A simple to use though inefficient http client which does not require closing. Useful for simple
 * applications or one off requests.
 */
public class SelfClosingApacheHttpClient implements HttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SelfClosingApacheHttpClient.class);

    private final LightblueClientConfiguration config;

    public SelfClosingApacheHttpClient(LightblueClientConfiguration config) {
        // Defensive copy because mutability...
        this.config = new LightblueClientConfiguration(config);
    }

    @Override
    public String executeRequest(LightblueRequest request, String baseUri) throws IOException {
        try (HttpClient httpClient = getClient()) {
            return httpClient.executeRequest(request, baseUri);
        }
    }

    private HttpClient getClient() {
        try {
            return LightblueApacheHttpClient.fromLightblueClientConfiguration(config);
        } catch (GeneralSecurityException | IOException e) {
            LOGGER.error("Error creating HTTP client: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        // Doesn't need to be closed to keep compatibility with legacy behavior.
    }
}

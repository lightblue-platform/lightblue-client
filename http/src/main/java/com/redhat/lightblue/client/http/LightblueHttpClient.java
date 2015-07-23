package com.redhat.lightblue.client.http;

import java.io.Closeable;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.transport.HttpTransport;
import com.redhat.lightblue.client.http.transport.JavaNetHttpTransport;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.DefaultLightblueResponse;
import com.redhat.lightblue.client.response.LightblueException;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.client.response.LightblueResponseParseException;
import com.redhat.lightblue.client.util.JSON;

public class LightblueHttpClient implements LightblueClient, Closeable {
    private final HttpTransport httpTransport;
    private final LightblueClientConfiguration configuration;
    private final ObjectMapper mapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(LightblueHttpClient.class);

    /**
     * This constructor will attempt to read the configuration from the default
     * properties file on the classpath.
     *
     * @see com.redhat.lightblue.client.PropertiesLightblueClientConfiguration
     */
    public LightblueHttpClient() {
        this(PropertiesLightblueClientConfiguration.fromDefault());
    }

    /**
     * This constructor will attempt to read the configuration from the
     * specified properties file on the file system.
     *
     * @see com.redhat.lightblue.client.PropertiesLightblueClientConfiguration
     */
    public LightblueHttpClient(String configFilePath) {
        this(PropertiesLightblueClientConfiguration.fromPath(Paths.get(configFilePath)));
    }

    /**
     * This constructor will use a copy of specified configuration object.
     */
    public LightblueHttpClient(LightblueClientConfiguration configuration) {
        this(configuration, JSON.getDefaultObjectMapper());
    }

    /**
     * This constructor will use a copy of specified configuration object and
     * object mapper.
     *
     * <p>
     * Without supplying an {@link com.fasterxml.jackson.databind.ObjectMapper}
     * explicitly, a default is shared among all threads ({@link #mapper}). It
     * is injectable here because of best practices: for further configuration
     * support and unit testing.
     */
    public LightblueHttpClient(LightblueClientConfiguration configuration, ObjectMapper mapper) {
        this(configuration, defaultHttpClientFromConfig(configuration), mapper);
    }

    public LightblueHttpClient(LightblueClientConfiguration configuration, HttpTransport httpTransport) {
        this(configuration, httpTransport, JSON.getDefaultObjectMapper());
    }

    public LightblueHttpClient(LightblueClientConfiguration configuration, HttpTransport httpTransport,
            ObjectMapper mapper) {
        this.httpTransport = Objects.requireNonNull(httpTransport, "httpTransport");
        this.mapper = Objects.requireNonNull(mapper, "mapper");

        // Make a defensive copy because configuration is mutable. This prevents alterations to the
        // config object from affecting this client after instantiation.
        Objects.requireNonNull(configuration, "configuration");
        this.configuration = new LightblueClientConfiguration(configuration);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.redhat.lightblue.client.LightblueClient#metadata(com.redhat.lightblue
     * .client.request.LightblueRequest)
     */
    @Override
    public LightblueResponse metadata(LightblueRequest lightblueRequest) {
        LOGGER.debug("Calling metadata service with lightblueRequest: " + lightblueRequest.toString());
        try {
            return callService(lightblueRequest, configuration.getMetadataServiceURI());
        } catch (Exception e) {
            throw new LightblueHttpClientException("Error sending lightblue request: " + lightblueRequest, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.redhat.lightblue.client.LightblueClient#data(com.redhat.lightblue.client
     * .request.LightblueRequest)
     */
    @Override
    public LightblueResponse data(LightblueRequest lightblueRequest) throws LightblueException {
        LOGGER.debug("Calling data service with lightblueRequest: " + lightblueRequest.toString());
        return callService(lightblueRequest, configuration.getDataServiceURI());
    }

    @Override
    public <T> T data(AbstractLightblueDataRequest lightblueRequest, Class<T> type) throws LightblueException {
        LightblueResponse response = data(lightblueRequest);
        try {
            return response.parseProcessed(type);
        } catch (RuntimeException | LightblueResponseParseException e) {
            throw new LightblueHttpClientException("Error sending lightblue request: " + lightblueRequest, e);
        }
    }

    @Override
    public void close() throws IOException {
        httpTransport.close();
    }

    protected LightblueResponse callService(LightblueRequest request, String baseUri) throws LightblueException {
        try {
            long t1 = new Date().getTime();

            String responseBody = httpTransport.executeRequest(request, baseUri);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Response received from service: " + responseBody);

                long t2 = new Date().getTime();
                LOGGER.debug("Call took " + (t2 - t1) + "ms");
            }
            return new DefaultLightblueResponse(responseBody, mapper);
        } catch (IOException e) {
            LOGGER.error("There was a problem calling the lightblue service", e);
            throw new LightblueException("There was a problem calling the lightblue service", null, e);
        }
    }

    private static HttpTransport defaultHttpClientFromConfig(LightblueClientConfiguration config) {
        try {
            return JavaNetHttpTransport.fromLightblueClientConfiguration(config);
        } catch (GeneralSecurityException | IOException e) {
            LOGGER.error("Error creating HTTP client: ", e);
            throw new RuntimeException(e);
        }
    }
}

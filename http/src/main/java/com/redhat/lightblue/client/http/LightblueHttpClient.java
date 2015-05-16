package com.redhat.lightblue.client.http;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.transport.HttpClient;
import com.redhat.lightblue.client.http.transport.SelfClosingApacheHttpClient;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.DefaultLightblueResponse;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.client.response.LightblueResponseParseException;
import com.redhat.lightblue.client.util.JSON;

public class LightblueHttpClient implements LightblueClient, Closeable {
    private final HttpClient httpClient;
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
        this(configuration, new SelfClosingApacheHttpClient(configuration), mapper);
    }

    public LightblueHttpClient(LightblueClientConfiguration configuration, HttpClient httpClient) {
        this(configuration, httpClient, JSON.getDefaultObjectMapper());
    }

    public LightblueHttpClient(LightblueClientConfiguration configuration, HttpClient httpClient,
            ObjectMapper mapper) {
        this.configuration = Objects.requireNonNull(configuration, "configuration");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    /**
     * @deprecated Use LightblueHttpClient(String configFilePath) if you want to
     * specify a config file location not on the classpath Use
     * LightblueHttpClient(LightblueClientConfiguration configuration) if you
     * don't want to use config files at all
     */
    @Deprecated
    public LightblueHttpClient(String dataServiceURI, String metadataServiceURI, Boolean useCertAuth) {
        configuration = new LightblueClientConfiguration();

        configuration.setDataServiceURI(dataServiceURI);
        configuration.setMetadataServiceURI(metadataServiceURI);
        configuration.setUseCertAuth(useCertAuth);

        this.httpClient = new SelfClosingApacheHttpClient(configuration);
        this.mapper = JSON.getDefaultObjectMapper();
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
    public LightblueResponse data(LightblueRequest lightblueRequest) {
        LOGGER.debug("Calling data service with lightblueRequest: " + lightblueRequest.toString());
        try {
            return callService(lightblueRequest, configuration.getDataServiceURI());
        } catch (Exception e) {
            throw new LightblueHttpClientException("Error sending lightblue request: " + lightblueRequest, e);
        }
    }

    @Override
    public <T> T data(AbstractLightblueDataRequest lightblueRequest, Class<T> type) throws IOException {
        LightblueResponse response = data(lightblueRequest);
        try {
            return response.parseProcessed(type);
        } catch (RuntimeException | LightblueResponseParseException e) {
            throw new LightblueHttpClientException("Error sending lightblue request: " + lightblueRequest, e);
        }
    }

    protected LightblueResponse callService(LightblueRequest request, String baseUri) {
        try {
            long t1 = new Date().getTime();

            String responseBody = httpClient.executeRequest(request, baseUri);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Response received from service: " + responseBody);

                long t2 = new Date().getTime();
                LOGGER.debug("Call took "+(t2-t1)+"ms");
            }
            return new DefaultLightblueResponse(responseBody, mapper);
        } catch (IOException e) {
            LOGGER.error("There was a problem calling the lightblue service", e);
            return new DefaultLightblueResponse("{\"error\":\"There was a problem calling the lightblue service\"}", mapper);
        }
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}

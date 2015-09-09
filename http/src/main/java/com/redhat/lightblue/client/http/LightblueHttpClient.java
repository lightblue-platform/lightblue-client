package com.redhat.lightblue.client.http;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.Locking;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.transport.HttpTransport;
import com.redhat.lightblue.client.http.transport.JavaNetHttpTransport;
import com.redhat.lightblue.client.request.AbstractBulkLightblueRequest;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.BulkLightblueResponse;
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

    private final class LockingRequest implements LightblueRequest {
        private final String uri;
        private final HttpMethod mth;

        public LockingRequest(String domain, String callerId, String resourceId, Long ttl, boolean ping, HttpMethod method) {
            StringBuilder b = new StringBuilder(128);
            b.append("lock/").append(domain).append('/').append(callerId).append('/').append(resourceId);
            if (ttl != null)
                b.append("?ttl=").append(ttl.toString());
            else if (ping)
                b.append('/').append("ping");
            uri = b.toString();
            this.mth = method;
        }

        @Override
        public String getBody() {
            return null;
        }

        @Override
        public JsonNode getBodyJson() {
            return null;
        }

        @Override
        public HttpMethod getHttpMethod() {
            return mth;
        }

        @Override
        public String getRestURI(String baseServiceURI) {
            StringBuilder b = new StringBuilder(128);
            b.append(baseServiceURI);
            if (!baseServiceURI.endsWith("/"))
                b.append('/');
            b.append(uri);
            return b.toString();
        }
    }

    private final class LockingImpl extends Locking {
        public LockingImpl(String domain) {
            super(domain);
        }

        @Override
        public boolean acquire(String callerId, String resourceId, Long ttl) throws LightblueException {
            try {
                LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, ttl, false, HttpMethod.PUT);
                String response = httpTransport.executeRequest(req, configuration.getDataServiceURI());
                JsonNode node = getResult(response);
                if (node != null)
                    return node.asBoolean();
                else
                    return false;
            } catch (IOException e) {
                LOGGER.error("There was a problem calling the lightblue service", e);
                throw new LightblueException("There was a problem calling the lightblue service", null, e);
            }
        }

        @Override
        public boolean release(String callerId, String resourceId) throws LightblueException {
            try {
                LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, null, false, HttpMethod.DELETE);
                String response = httpTransport.executeRequest(req, configuration.getDataServiceURI());
                JsonNode node = getResult(response);
                if (node != null)
                    return node.asBoolean();
                else
                    return false;
            } catch (IOException e) {
                LOGGER.error("There was a problem calling the lightblue service", e);
                throw new LightblueException("There was a problem calling the lightblue service", null, e);
            }
        }

        @Override
        public int getLockCount(String callerId, String resourceId) throws LightblueException {
            try {
                LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, null, false, HttpMethod.GET);
                String response = httpTransport.executeRequest(req, configuration.getDataServiceURI());
                JsonNode node = getResult(response);
                if (node != null)
                    return node.asInt();
                else
                    return 0;
            } catch (IOException e) {
                LOGGER.error("There was a problem calling the lightblue service", e);
                throw new LightblueException("There was a problem calling the lightblue service", null, e);
            }
        }

        @Override
        public boolean ping(String callerId, String resourceId) throws LightblueException {
            try {
                LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, null, true, HttpMethod.PUT);
                String response = httpTransport.executeRequest(req, configuration.getDataServiceURI());
                JsonNode node = getResult(response);
                if (node != null)
                    return node.asBoolean();
                else
                    return false;
            } catch (IOException e) {
                LOGGER.error("There was a problem calling the lightblue service", e);
                throw new LightblueException("There was a problem calling the lightblue service", null, e);
            }
        }

        private JsonNode getResult(String response) throws IOException {
            JsonNode node = JSON.getDefaultObjectMapper().readTree(response);
            if (node instanceof ObjectNode)
                return ((ObjectNode) node).get("result");
            return null;
        }
    }

    /**
     * This constructor will attempt to read the configuration from the default properties file on the classpath.
     *
     * @see com.redhat.lightblue.client.PropertiesLightblueClientConfiguration
     */
    public LightblueHttpClient() {
        this(PropertiesLightblueClientConfiguration.fromDefault());
    }

    /**
     * This constructor will attempt to read the configuration from the specified properties file on the file system.
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
     * This constructor will use a copy of specified configuration object and object mapper.
     *
     * <p>
     * Without supplying an {@link com.fasterxml.jackson.databind.ObjectMapper} explicitly, a default is shared among all threads ({@link #mapper}). It is injectable here because
     * of best practices: for further configuration support and unit testing.
     */
    public LightblueHttpClient(LightblueClientConfiguration configuration, ObjectMapper mapper) {
        this(configuration, defaultHttpClientFromConfig(configuration), mapper);
    }

    public LightblueHttpClient(LightblueClientConfiguration configuration, HttpTransport httpTransport) {
        this(configuration, httpTransport, JSON.getDefaultObjectMapper());
    }

    public LightblueHttpClient(LightblueClientConfiguration configuration, HttpTransport httpTransport, ObjectMapper mapper) {
        this.httpTransport = Objects.requireNonNull(httpTransport, "httpTransport");
        this.mapper = Objects.requireNonNull(mapper, "mapper");

        // Make a defensive copy because configuration is mutable. This prevents alterations to the
        // config object from affecting this client after instantiation.
        Objects.requireNonNull(configuration, "configuration");
        this.configuration = new LightblueClientConfiguration(configuration);
    }

    /**
     * @deprecated Use LightblueHttpClient(String configFilePath) if you want to specify a config file location not on the classpath Use
     *             LightblueHttpClient(LightblueClientConfiguration configuration) if you don't want to use config files at all
     */
    @Deprecated
    public LightblueHttpClient(String dataServiceURI, String metadataServiceURI, Boolean useCertAuth) {
        configuration = new LightblueClientConfiguration();

        configuration.setDataServiceURI(dataServiceURI);
        configuration.setMetadataServiceURI(metadataServiceURI);
        configuration.setUseCertAuth(useCertAuth);

        this.httpTransport = defaultHttpClientFromConfig(configuration);
        this.mapper = JSON.getDefaultObjectMapper();
    }

    @Override
    public Locking getLocking(String domain) {
        return new LockingImpl(domain);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.redhat.lightblue.client.LightblueClient#metadata(com.redhat.lightblue .client.request.LightblueRequest)
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
     * @see com.redhat.lightblue.client.LightblueClient#data(com.redhat.lightblue.client .request.LightblueRequest)
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
    public BulkLightblueResponse dataBulk(AbstractBulkLightblueRequest<AbstractLightblueDataRequest> request) throws LightblueException {
        long t1 = new Date().getTime();
        try {
            String responseBody = httpTransport.executeRequest(request, configuration.getDataServiceURI());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Response received from service: " + responseBody);

                long t2 = new Date().getTime();
                LOGGER.debug("Call took " + (t2 - t1) + "ms");
            }
            return new BulkLightblueResponse(responseBody, request);
        } catch (IOException e) {
            LOGGER.error("There was a problem calling the lightblue service", e);
            throw new LightblueException("There was a problem calling the lightblue service", null, e);
        } catch (LightblueResponseParseException e) {
            throw new LightblueException("There was a problem parsing the response", null, e);
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

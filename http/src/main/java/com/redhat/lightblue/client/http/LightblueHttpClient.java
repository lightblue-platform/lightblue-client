package com.redhat.lightblue.client.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.Locking;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.transport.HttpTransport;
import com.redhat.lightblue.client.http.transport.JavaNetHttpTransport;
import com.redhat.lightblue.client.request.AbstractDataBulkRequest;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.DefaultLightblueBulkDataResponse;
import com.redhat.lightblue.client.response.DefaultLightblueDataResponse;
import com.redhat.lightblue.client.response.DefaultLightblueMetadataResponse;
import com.redhat.lightblue.client.response.LightblueBulkResponseException;
import com.redhat.lightblue.client.response.LightblueDataResponse;
import com.redhat.lightblue.client.response.LightblueParseException;
import com.redhat.lightblue.client.response.LightblueResponseException;
import com.redhat.lightblue.client.response.lock.LockResponse;
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
            try {
                b.append("lock/")
                    .append(URLEncoder.encode(domain, StandardCharsets.UTF_8.name())).append('/')
                    .append(URLEncoder.encode(callerId, StandardCharsets.UTF_8.name())).append('/')
                    .append(URLEncoder.encode(resourceId, StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                //Shouldn't happen.
                throw new RuntimeException("A bad Charset was used.", e);
            }
            if (ttl != null) {
                b.append("?ttl=").append(ttl.toString());
            } else if (ping) {
                b.append('/').append("ping");
            }
            uri = b.toString();
            mth = method;
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
            if (!baseServiceURI.endsWith("/")) {
                b.append('/');
            }
            b.append(uri);
            return b.toString();
        }
    }

    private final class LockingImpl extends Locking {

        public LockingImpl(String domain) {
            super(domain);
        }

        @Override
        public boolean acquire(String callerId, String resourceId, Long ttl)
                throws LightblueParseException, LightblueHttpClientException, LightblueResponseException, LightblueException {
            LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, ttl, false, HttpMethod.PUT);
            LockResponse response = new LockResponse(
                    callService(req, configuration.getDataServiceURI()));

            if (response.getJson() != null) {
                return response.parseAsBoolean();
            } else {
                return false;
            }
        }

        @Override
        public boolean release(String callerId, String resourceId)
                throws LightblueParseException, LightblueHttpClientException, LightblueResponseException, LightblueException {
            LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, null, false, HttpMethod.DELETE);
            LockResponse response = new LockResponse(
                    callService(req, configuration.getDataServiceURI()));

            if (response.getJson() != null) {
                return response.parseAsBoolean();
            } else {
                return false;
            }
        }

        @Override
        public int getLockCount(String callerId, String resourceId)
                throws LightblueParseException, LightblueHttpClientException, LightblueResponseException, LightblueException {
            LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, null, false, HttpMethod.GET);
            LockResponse response = new LockResponse(
                    callService(req, configuration.getDataServiceURI()));

            if (response.getJson() != null) {
                return response.parseAsInt();
            } else {
                return 0;
            }
        }

        @Override
        public boolean ping(String callerId, String resourceId)
                throws LightblueParseException, LightblueHttpClientException, LightblueResponseException, LightblueException {
            LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, null, true, HttpMethod.PUT);
            LockResponse response = new LockResponse(
                    callService(req, configuration.getDataServiceURI()));

            if (response.getJson() != null) {
                return response.parseAsBoolean();
            } else {
                return false;
            }
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
    public DefaultLightblueMetadataResponse metadata(LightblueRequest lightblueRequest) throws LightblueParseException, LightblueResponseException, LightblueHttpClientException, LightblueException {
        LOGGER.debug("Calling metadata service with lightblueRequest: {}", lightblueRequest.toString());

        return new DefaultLightblueMetadataResponse(
                callService(lightblueRequest, configuration.getMetadataServiceURI()),
                mapper);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.redhat.lightblue.client.LightblueClient#data(com.redhat.lightblue.client .request.LightblueRequest)
     */
    @Override
    public DefaultLightblueDataResponse data(LightblueRequest lightblueRequest)
            throws LightblueParseException, LightblueResponseException, LightblueHttpClientException, LightblueException {
        LOGGER.debug("Calling data service with lightblueRequest: {}", lightblueRequest.toString());
        return new DefaultLightblueDataResponse(
                callService(lightblueRequest, configuration.getDataServiceURI()),
                mapper);
    }

    @Override
    public <T> T data(LightblueRequest lightblueRequest, Class<T> type)
            throws LightblueParseException, LightblueResponseException, LightblueHttpClientException, LightblueException {
        LightblueDataResponse response = data(lightblueRequest);

        return response.parseProcessed(type);
    }

    @Override
    public DefaultLightblueBulkDataResponse bulkData(AbstractDataBulkRequest<AbstractLightblueDataRequest> lightblueRequests) throws LightblueHttpClientException, LightblueBulkResponseException, LightblueParseException, LightblueException {
        LOGGER.debug("Calling data service with lightblueRequest: {}", lightblueRequests.toString());

        String response = callService(lightblueRequests, configuration.getDataServiceURI());

        try {
            return new DefaultLightblueBulkDataResponse(response, mapper, lightblueRequests);
        } catch (LightblueParseException e) {
            throw new LightblueParseException("Unable to parse response " + response, e);
        }
    }

    @Override
    public void close() throws IOException {
        httpTransport.close();
    }

    protected String callService(LightblueRequest request, String baseUri) throws LightblueHttpClientException {
        long t1 = System.currentTimeMillis();

        String responseBody = httpTransport.executeRequest(request, baseUri);

        LOGGER.debug("Response received from service: {}", responseBody);

        long t2 = new Date().getTime();
        LOGGER.debug("Call took {}ms", t2 - t1);

        return responseBody;
    }

    private static HttpTransport defaultHttpClientFromConfig(LightblueClientConfiguration config) {
        try {
            return JavaNetHttpTransport.fromLightblueClientConfiguration(config);
        } catch (Exception e) {
            LOGGER.error("Error creating HTTP client: ", e);
            throw new RuntimeException(e);
        }
    }

}

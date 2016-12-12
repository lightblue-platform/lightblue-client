package com.redhat.lightblue.client.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.Locking;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.transport.HttpResponse;
import com.redhat.lightblue.client.http.transport.HttpTransport;
import com.redhat.lightblue.client.http.transport.JavaNetHttpTransport;
import com.redhat.lightblue.client.request.DataBulkRequest;
import com.redhat.lightblue.client.request.LightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

public class LightblueHttpClient implements LightblueClient, Closeable {
    private final HttpTransport httpTransport;
    private final LightblueClientConfiguration configuration;
    private final ObjectMapper mapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(LightblueHttpClient.class);

    private final class LockingRequest extends LightblueRequest {
        private String operation;
        private String domain;
        private String callerId;
        private String resourceId;
        private Long ttl;
        boolean ping;

        public LockingRequest(String operation, String domain, String callerId, String resourceId, Long ttl,
                              boolean ping, HttpMethod method) {
            super(method);
            this.operation = operation;
            this.domain = domain;
            this.callerId = callerId;
            this.resourceId = resourceId;
            this.ttl = ttl;
            this.ping = ping;
        }

        @Override
        public JsonNode getBodyJson() {
            ObjectNode root = JsonNodeFactory.instance.objectNode();
            root.set("operation", JsonNodeFactory.instance.textNode(operation));
            root.set("domain", JsonNodeFactory.instance.textNode(domain));
            root.set("callerId", JsonNodeFactory.instance.textNode(callerId));
            root.set("resourceId", JsonNodeFactory.instance.textNode(resourceId));
            if(null != ttl) {
                root.set("ttl", JsonNodeFactory.instance.numberNode(ttl));
            }
            return root;
        }

        @Override
        public String getRestURI(String baseServiceURI) {
            StringBuilder b = new StringBuilder(128);
            b.append(baseServiceURI);
            if (!baseServiceURI.endsWith("/")) {
                b.append('/');
            }
            b.append("lock");
            return b.toString();
        }
    }

    private final class LockingImpl extends Locking {
        public LockingImpl(String domain) {
            super(domain);
        }

        @Override
        public boolean acquire(String callerId, String resourceId, Long ttl) throws LightblueException {
            LightblueRequest req = new LockingRequest("acquire", getDomain(), callerId, resourceId, ttl, false, HttpMethod.POST);
            HttpResponse r = callService(req, configuration.getDataServiceURI());
            LockResponse response = new LockResponse(r.getBody(), r.getHeaders());

            if (response.getJson() != null) {
                return response.parseAsBoolean();
            } else {
                return false;
            }
        }

        @Override
        public boolean release(String callerId, String resourceId) throws LightblueException {
            LightblueRequest req = new LockingRequest(
                    "release", getDomain(), callerId, resourceId, null, false, HttpMethod.POST);
            HttpResponse r = callService(req, configuration.getDataServiceURI());
            LockResponse response = new LockResponse(r.getBody(), r.getHeaders());

            if (response.getJson() != null) {
                return response.parseAsBoolean();
            } else {
                return false;
            }
        }

        @Override
        public int getLockCount(String callerId, String resourceId) throws LightblueException {
            LightblueRequest req = new LockingRequest(
                    "count", getDomain(), callerId, resourceId, null, false, HttpMethod.POST);
            HttpResponse r = callService(req, configuration.getDataServiceURI());
            LockResponse response = new LockResponse(r.getBody(), r.getHeaders());

            if (response.getJson() != null) {
                return response.parseAsInt();
            } else {
                return 0;
            }
        }

        @Override
        public boolean ping(String callerId, String resourceId) throws LightblueException {
            LightblueRequest req = new LockingRequest(
                    "ping", getDomain(), callerId, resourceId, null, true, HttpMethod.POST);
            HttpResponse r = callService(req, configuration.getDataServiceURI());
            LockResponse response = new LockResponse(r.getBody(), r.getHeaders());

            if (response.getJson() != null) {
                return response.parseAsBoolean();
            } else {
                return false;
            }
        }

    }

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
    public DefaultLightblueMetadataResponse metadata(LightblueMetadataRequest lightblueRequest) throws LightblueParseException, LightblueResponseException, LightblueHttpClientException, LightblueException {
        HttpResponse response = callService(lightblueRequest, configuration.getMetadataServiceURI());
        return new DefaultLightblueMetadataResponse(
                response.getBody(),
                response.getHeaders(),
                mapper);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.redhat.lightblue.client.LightblueClient#data(com.redhat.lightblue.client .request.LightblueRequest)
     */
    @Override
    public DefaultLightblueDataResponse data(LightblueDataRequest lightblueRequest)
            throws LightblueParseException, LightblueResponseException, LightblueHttpClientException, LightblueException {
        if (!lightblueRequest.hasExecution()) {
            lightblueRequest.execution(configuration.getExecution());
        }

        HttpResponse response = callService(lightblueRequest, configuration.getDataServiceURI());
        return new DefaultLightblueDataResponse(
                response.getBody(),
                response.getHeaders(),
                mapper);
    }

    @Override
    public <T> T data(LightblueDataRequest lightblueRequest, Class<T> type)
            throws LightblueParseException, LightblueResponseException, LightblueHttpClientException, LightblueException {
        LightblueDataResponse response = data(lightblueRequest);

        return response.parseProcessed(type);
    }

    @Override
    public DefaultLightblueBulkDataResponse bulkData(DataBulkRequest lightblueRequests) throws LightblueHttpClientException, LightblueBulkResponseException, LightblueParseException, LightblueException {
        HttpResponse response = callService(lightblueRequests, configuration.getDataServiceURI());

        try {
            return new DefaultLightblueBulkDataResponse(
                    response.getBody(),
                    response.getHeaders(),
                    mapper,
                    lightblueRequests);
        } catch (LightblueParseException e) {
            throw new LightblueParseException("Unable to parse response " + response, e);
        }
    }

    @Override
    public void close() throws IOException {
        httpTransport.close();
    }

    protected HttpResponse callService(LightblueRequest request, String baseUri) throws LightblueHttpClientException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calling service: {}", request.toString());
        }

        long t1 = System.currentTimeMillis();

        HttpResponse response = httpTransport.executeRequest(request, baseUri);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Response received from service: {}", response.getBody());
        }

        long t2 = new Date().getTime();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Call took {}ms", t2 - t1);
        }

        return response;
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

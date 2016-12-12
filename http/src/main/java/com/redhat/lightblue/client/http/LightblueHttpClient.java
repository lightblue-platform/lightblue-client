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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

public class LightblueHttpClient implements LightblueClient, Closeable {
    private final HttpTransport httpTransport;
    private final LightblueClientConfiguration configuration;
    private final ObjectMapper mapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(LightblueHttpClient.class);

    private final class LockingRequest extends LightblueRequest {
        private String domain;
        private String callerId;
        private String resourceId;
        private Long ttl;
        boolean ping;
        Boolean usePost = false;

        public LockingRequest(String domain, String callerId, String resourceId, Long ttl, boolean ping, HttpMethod method) {
            super(method);
            this.domain = domain;
            this.callerId = callerId;
            this.resourceId = resourceId;
            this.ttl = ttl;
            this.ping = ping;

            if(HttpMethod.POST.equals(method)) {
                usePost = true;
            }
        }

        @Override
        public JsonNode getBodyJson() {
            if(usePost) {
                ObjectNode root = JsonNodeFactory.instance.objectNode();
                root.set("domain", JsonNodeFactory.instance.textNode(domain));
                root.set("callerId", JsonNodeFactory.instance.textNode(callerId));
                root.set("resourceId", JsonNodeFactory.instance.textNode(resourceId));
                root.set("ttl", JsonNodeFactory.instance.textNode(resourceId));
                return root;
            } else {
                return null;
            }
        }

        @Override
        public String getRestURI(String baseServiceURI) {
            StringBuilder b = new StringBuilder(128);
            b.append(baseServiceURI);
            if (!baseServiceURI.endsWith("/")) {
                b.append('/');
            }
            b.append(usePost ? getRestURIWithPost() : getRestURI());
            return b.toString();
        }

        private String getRestURI() {
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
            return b.toString();
        }

        private String getRestURIWithPost() {
            return "lock/";
        }
    }

    private final class LockingImpl extends Locking {

        Boolean usePost = false;

        public LockingImpl(String domain) {
            super(domain);
        }

        /**
         * This implementation allows you to use POST requests for all locking activity, in case
         * anything you want to lock by has non-allowed URL characters (like forward slash /)
         *
         * @param domain
         * @param usePost
         */
        public LockingImpl(String domain, Boolean usePost) {
            super(domain);
            this.usePost = usePost;
        }

        @Override
        public boolean acquire(String callerId, String resourceId, Long ttl) throws LightblueException {
            LightblueRequest req = new LockingRequest(
                    getDomain(), callerId, resourceId, ttl, false,
                    usePost ? HttpMethod.POST : HttpMethod.PUT);
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
                    getDomain(), callerId, resourceId, null, false,
                    usePost ? HttpMethod.POST : HttpMethod.DELETE);
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
                    getDomain(), callerId, resourceId, null, false,
                    usePost ? HttpMethod.POST : HttpMethod.GET);
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
                    getDomain(), callerId, resourceId, null, true,
                    usePost ? HttpMethod.POST : HttpMethod.PUT);
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

    @Override
    public Locking getLocking(String domain, Boolean usePost) {
        return new LockingImpl(domain, usePost);
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

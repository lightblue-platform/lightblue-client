package com.redhat.lightblue.client.http;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.Locking;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.transport.HttpTransport;
import com.redhat.lightblue.client.http.transport.JavaNetHttpTransport;
import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;
import com.redhat.lightblue.client.request.AbstractDataBulkRequest;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.DefaultLightblueBulkDataResponse;
import com.redhat.lightblue.client.response.DefaultLightblueDataResponse;
import com.redhat.lightblue.client.response.DefaultLightblueMetadataResponse;
import com.redhat.lightblue.client.response.LightblueBulkResponseException;
import com.redhat.lightblue.client.response.LightblueDataResponse;
import com.redhat.lightblue.client.response.LightblueErrorResponse;
import com.redhat.lightblue.client.response.LightblueParseException;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.client.response.LightblueResponseException;
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

    private final class LockingImpl extends Locking implements LightblueResponse, LightblueErrorResponse {
        private String text;
        private JsonNode json;

        public LockingImpl(String domain) {
            super(domain);
        }

        @Override
        public boolean acquire(String callerId, String resourceId, Long ttl)
                throws LightblueParseException, LightblueHttpClientException, LightblueResponseException {
            LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, ttl, false, HttpMethod.PUT);
            parseResponse(
                    callService(req, configuration.getDataServiceURI()));
            if (hasAnyErrors()) {
                throw new LightblueResponseException("Unable to acquire lock.", this);
            }
            if (getJson() != null) {
                return parseResultNode().asBoolean();
            } else {
                return false;
            }
        }

        @Override
        public boolean release(String callerId, String resourceId)
                throws LightblueParseException, LightblueHttpClientException, LightblueResponseException {
            LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, null, false, HttpMethod.DELETE);
            parseResponse(
                    callService(req, configuration.getDataServiceURI()));
            if (hasAnyErrors()) {
                throw new LightblueResponseException("Unable to release lock.", this);
            }
            if (getJson() != null) {
                return parseResultNode().asBoolean();
            } else {
                return false;
            }
        }

        @Override
        public int getLockCount(String callerId, String resourceId)
                throws LightblueParseException, LightblueHttpClientException, LightblueResponseException {
            LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, null, false, HttpMethod.GET);
            parseResponse(
                    callService(req, configuration.getDataServiceURI()));
            if (hasAnyErrors()) {
                throw new LightblueResponseException("Unable to get lock count.", this);
            }
            if (getJson() != null) {
                return parseResultNode().asInt();
            } else {
                return 0;
            }
        }

        @Override
        public boolean ping(String callerId, String resourceId)
                throws LightblueParseException, LightblueHttpClientException, LightblueResponseException {
            LightblueRequest req = new LockingRequest(getDomain(), callerId, resourceId, null, true, HttpMethod.PUT);
            parseResponse(
                    callService(req, configuration.getDataServiceURI()));
            if (hasAnyErrors()) {
                throw new LightblueResponseException("Unable to ping lock.", this);
            }
            if (getJson() != null) {
                return parseResultNode().asBoolean();
            } else {
                return false;
            }
        }

        private void parseResponse(String response) throws LightblueParseException {
            text = response;

            try {
                json = JSON.getDefaultObjectMapper().readTree(text);
            } catch (IOException e) {
                throw new LightblueParseException(e);
            }
        }

        private JsonNode parseResultNode() throws LightblueParseException {
            if (!(getJson() instanceof ObjectNode)) {
                throw new LightblueParseException("Unable to parse json: " + getJson());
            }

            return ((ObjectNode) getJson()).get("result");
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public JsonNode getJson() {
            return json;
        }

        @Override
        public boolean hasDataErrors() {
            if (getJson() == null) {
                return false;
            }

            JsonNode err = getJson().get("dataErrors");
            return err != null && !(err instanceof NullNode) && err.size() > 0;
        }

        @Override
        public boolean hasLightblueErrors() {
            if (getJson() == null) {
                return false;
            }

            JsonNode objectTypeNode = getJson().get("status");
            if (objectTypeNode != null && (objectTypeNode.textValue().equalsIgnoreCase(
                    "error") || objectTypeNode.textValue().equalsIgnoreCase(
                            "partial"))) {
                return true;
            }

            JsonNode err = getJson().get("errors");
            return err != null && !(err instanceof NullNode) && err.size() > 0;
        }

        public boolean hasAnyErrors() {
            return hasDataErrors() || hasLightblueErrors();
        }

        @Override
        public DataError[] getDataErrors() {
            List<DataError> list = new ArrayList<>();
            if (getJson() == null) {
                return null;
            }
            JsonNode err = getJson().get("dataErrors");
            if (err instanceof ObjectNode) {
                list.add(DataError.fromJson((ObjectNode) err));
            } else if (err instanceof ArrayNode) {
                for (Iterator<JsonNode> itr = ((ArrayNode) err).elements(); itr.hasNext();) {
                    list.add(DataError.fromJson((ObjectNode) itr.next()));
                }
            } else {
                return null;
            }
            return list.toArray(new DataError[list.size()]);
        }

        @Override
        public Error[] getLightblueErrors() {
            List<Error> list = new ArrayList<>();
            if (getJson() == null) {
                return null;
            }
            JsonNode err = getJson().get("errors");
            if (err instanceof ObjectNode) {
                list.add(Error.fromJson(err));
            } else if (err instanceof ArrayNode) {
                for (Iterator<JsonNode> itr = ((ArrayNode) err).elements(); itr.hasNext();) {
                    list.add(Error.fromJson(itr.next()));
                }
            } else {
                return null;
            }
            return list.toArray(new Error[list.size()]);
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
    public DefaultLightblueMetadataResponse metadata(LightblueRequest lightblueRequest) throws LightblueParseException, LightblueResponseException, LightblueHttpClientException {
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
    public DefaultLightblueDataResponse data(LightblueRequest lightblueRequest) throws LightblueParseException, LightblueResponseException, LightblueHttpClientException {
        LOGGER.debug("Calling data service with lightblueRequest: {}", lightblueRequest.toString());
        return new DefaultLightblueDataResponse(
                callService(lightblueRequest, configuration.getDataServiceURI()),
                mapper);
    }

    @Override
    public <T> T data(AbstractLightblueDataRequest lightblueRequest, Class<T> type) throws LightblueParseException, LightblueResponseException, LightblueHttpClientException {
        LightblueDataResponse response = data(lightblueRequest);

        return response.parseProcessed(type);
    }

    @Override
    public DefaultLightblueBulkDataResponse bulkData(AbstractDataBulkRequest<AbstractLightblueDataRequest> lightblueRequests) throws LightblueHttpClientException, LightblueBulkResponseException, LightblueParseException {
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

package com.redhat.lightblue.client.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.Literal;
import com.redhat.lightblue.client.Locking;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.ResultStream;
import com.redhat.lightblue.client.ResultStream.ForEachDoc;
import com.redhat.lightblue.client.ResultStream.StreamDoc;
import com.redhat.lightblue.client.http.transport.HttpResponse;
import com.redhat.lightblue.client.http.transport.HttpTransport;
import com.redhat.lightblue.client.http.transport.JavaNetHttpTransport;
import com.redhat.lightblue.client.request.DataBulkRequest;
import com.redhat.lightblue.client.request.LightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueDiagnosticsRequest;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.response.DefaultLightblueBulkDataResponse;
import com.redhat.lightblue.client.response.DefaultLightblueDataResponse;
import com.redhat.lightblue.client.response.DefaultLightblueDiagnosticsResponse;
import com.redhat.lightblue.client.response.DefaultLightblueMetadataResponse;
import com.redhat.lightblue.client.response.LightblueBulkResponseException;
import com.redhat.lightblue.client.response.LightblueDataResponse;
import com.redhat.lightblue.client.response.LightblueDiagnosticsResponse;
import com.redhat.lightblue.client.response.LightblueParseException;
import com.redhat.lightblue.client.response.LightblueResponseException;
import com.redhat.lightblue.client.response.ResultMetadata;
import com.redhat.lightblue.client.response.lock.LockResponse;
import com.redhat.lightblue.client.util.JSON;

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

    private static class StreamingDataFindRequest extends DataFindRequest {
        public StreamingDataFindRequest(DataFindRequest r) {
            super(r);
        }

        @Override
        public String getRestURI(String baseServiceURI) {
            return super.getRestURI(baseServiceURI)+"?stream=true";
        }
    }
    
    private  class StreamingClosure implements ResultStream.RequestCl {
        private final StreamingDataFindRequest req;

        StreamingClosure(DataFindRequest r) {
            this.req=new StreamingDataFindRequest(r);
        }
        @Override
        public void submitAndIterate(final ResultStream.ForEachDoc f) throws LightblueException {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Calling service (streaming): {}", req.toString());
            }
            InputStream stream = httpTransport.executeRequestGetStream(req, configuration.getDataServiceURI());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Response received from service, streaming");
            }
            if(stream!=null) {
                try {
                    // StreamParser handles both of the following cases:
                    //   1- results are being streamed
                    //   2- results are *not* being streamed, and there is only one json document
                    StreamJsonParser streamParser=new StreamJsonParser(stream,mapper) {
                            @Override
                            public boolean documentCompleted(ObjectNode n) {
                                JsonNode status=n.get("status");
                                if(status!=null) {
                                    // This is a header, or it has the header in it
                                    ResultStream.ResponseHeader header=ResultStream.ResponseHeader.fromJson(n);
                                    f.processResponseHeader(header);
                                }
                                JsonNode processed=n.get("processed");
                                if(processed instanceof ArrayNode) {
                                    // This has multiple docs in it
                                    // The results are not being streamed
                                    LOGGER.debug("Results are not being streamed, retrieving all results");
                                    List<ResultMetadata> lrmd=new ArrayList<ResultMetadata>();
                                    JsonNode rmd=n.get("resultMetadata");
                                    if(rmd instanceof ArrayNode) {
                                        for(Iterator<JsonNode> itr=rmd.elements();itr.hasNext();) {
                                            lrmd.add(ResultMetadata.fromJson((ObjectNode)itr.next()));
                                        }
                                    }
                                    int k=0;
                                    for(Iterator<JsonNode> itr=processed.elements();itr.hasNext();k++) {
                                        JsonNode docNode=itr.next();
                                        ResultMetadata md;
                                        if(lrmd.size()>k) {
                                            md=lrmd.get(k);
                                        } else {
                                            md=null;
                                        }
                                        ResultStream.StreamDoc doc=new ResultStream.StreamDoc();
                                        doc.doc=(ObjectNode)docNode;
                                        doc.resultMetadata=md;
                                        doc.lastDoc=!itr.hasNext();
                                        if(!f.processDocument(doc))
                                            return false;
                                    }
                                } else if(processed instanceof ObjectNode) {
                                    // Single document
                                    // The results are being streamed
                                    LOGGER.debug("Retrieved one document from stream");
                                    ResultStream.StreamDoc doc=ResultStream.StreamDoc.fromJson(n);
                                    if(!f.processDocument(doc)) {
                                        LOGGER.debug("Stream processing cancelled");
                                        return false;
                                    }
                                }
                                return true;
                            }
                        };
                    streamParser.parse();
                    
                } catch (IOException e) {
                    throw new LightblueException(e);
                } finally {
                    try {
                        stream.close();
                    } catch (Exception e) {}
                }
            } 
        }
    }

    @Override
    public ResultStream prepareFind(DataFindRequest req) throws LightblueException {
        return new ResultStream(new StreamingClosure(req),mapper);
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

    public static void main(String[] args) throws LightblueException {

        LightblueClientConfiguration p = PropertiesLightblueClientConfiguration
                .fromPath(Paths.get("/home/mpatercz/redhat/lightblue-certs/client/lightblue-client-dev.properties"));
//                .fromPath(Paths.get("/home/mpatercz/redhat/lightblue-certs/client/lightblue-client-devunit.properties"));

        LightblueClient client = new LightblueHttpClient(p);

        DataFindRequest r = new DataFindRequest("subscription");
        r.select(new Projection[] { Projection.includeField("_id"),
                Projection.array("subscriptionProducts", Query.withValue("inactiveDate", Query.eq, Literal.value(null))) });
        r.where(Query.withValue("_id=0"));


//        StreamingDataFindRequest sR = new StreamingDataFindRequest(r);

//        System.out.println(sR);

//        str.run(new ForEachDoc() {
//
//            @Override
//            public boolean processDocument(StreamDoc doc) {
//                System.out.println(doc.doc.toString());
//                System.out.println("size="+((ArrayNode)doc.doc.get("subscriptionProducts")).size());
//                return true;
//            }
//        });

        LightblueDataResponse response = client.data(r);
        System.out.println(response.getText());




    }
    
    @Override
    public LightblueDiagnosticsResponse diagnostics()
            throws LightblueParseException, LightblueResponseException, LightblueException {

        HttpResponse response = callService(new LightblueDiagnosticsRequest(), configuration.getDataServiceURI());

        try {
            return new DefaultLightblueDiagnosticsResponse(response.getBody(), response.getHeaders(), mapper);

        } catch (LightblueParseException e) {
            throw new LightblueParseException("Unable to parse response " + response, e);
        }
    }
}

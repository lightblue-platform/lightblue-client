package com.redhat.lightblue.client.http.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.compress.lzf.LZFInputStream;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.LightblueClientConfiguration.Compression;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.http.LightblueHttpClientException;
import com.redhat.lightblue.client.http.auth.SslSocketFactories;
import com.redhat.lightblue.client.request.LightblueRequest;

/**
 * An {@link HttpTransport} which uses vanilla java.net classes to execute a
 * {@link LightblueRequest}. Recommended to use in a servlet environment which
 * is already multi-threaded by virtue of the application server. This class is
 * thread safe, so you should use an application-scoped
 * {@link com.redhat.lightblue.client.http.LightblueHttpClient} backed by an
 * instance of this class, which is the default behavior if you have not passed
 * in a different {@code HttpTransport}.
 *
 * <p>
 * This implementation takes advantage of HTTP persistent connections as per:
 * <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html">http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html</a>.
 * Sockets are left open to be reused after each request per Java SDK semantics.
 */
public class JavaNetHttpTransport implements HttpTransport {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final ConnectionFactory connectionFactory;
    private final SSLSocketFactory sslSocketFactory;
    private Compression compression;

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaNetHttpTransport.class);

    public JavaNetHttpTransport() {
        this((SSLSocketFactory) null);
    }

    public JavaNetHttpTransport(ConnectionFactory connectionFactory) {
        this(connectionFactory, null, Compression.LZF);
    }

    public JavaNetHttpTransport(SSLSocketFactory sslSocketFactory) {
        this(new UrlConnectionFactory(), sslSocketFactory, Compression.LZF);
    }

    /**
     * @param connectionFactory Injectable for testing. To actually make HTTP
     * requests, use another constructor or pass {@link UrlConnectionFactory}.
     * @param sslSocketFactory May be null, indicating
     * {@link SSLSocketFactory#getDefault()}} will be used.
     * @param compression Compression method to advertise to the endpoint. Does
     * not mean that compression will be actially used.
     */
    public JavaNetHttpTransport(ConnectionFactory connectionFactory, SSLSocketFactory sslSocketFactory, Compression compression) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "connectionFactory");
        this.sslSocketFactory = sslSocketFactory;
        this.compression = compression;
    }

    /**
     * @param connectionFactory Injectable for testing. To actually make HTTP
     * requests, use another constructor or pass {@link UrlConnectionFactory}.
     * @param sslSocketFactory May be null, indicating
     * {@link SSLSocketFactory#getDefault()}} will be used.
     */
    public JavaNetHttpTransport(ConnectionFactory connectionFactory, SSLSocketFactory sslSocketFactory) {
        this(connectionFactory, sslSocketFactory, Compression.LZF);
    }

    public static JavaNetHttpTransport fromLightblueClientConfiguration(LightblueClientConfiguration config)
            throws GeneralSecurityException, LightblueHttpClientException {
        try {
            Objects.requireNonNull(config, "config");

            SSLSocketFactory sslSocketFactory = config.useCertAuth()
                    ? SslSocketFactories.javaNetSslSocketFactory(config)
                    : null;

            return new JavaNetHttpTransport(new UrlConnectionFactory(), sslSocketFactory, config.getCompression());
        } catch (IOException e) {
            throw new LightblueHttpClientException(e);
        }
    }

    @Override
    public Response executeRequest(LightblueRequest request, String baseUri) throws LightblueHttpClientException {
        try {
            String url = request.getRestURI(baseUri);
            LOGGER.debug("Executing request, url={}", url);
            HttpURLConnection connection = connectionFactory.openConnection(url);

            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) connection;

                if (sslSocketFactory != null) {
                    httpsUrlConnection.setSSLSocketFactory(sslSocketFactory);
                }
            }

            connection.setRequestMethod((request.getHttpMethod() != null ? request.getHttpMethod().toString() : HttpMethod.GET.name()));
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Charset", "utf-8");

            if (compression == Compression.LZF) {
                LOGGER.debug("Advertising lzf decoding capabilities to lightblue");
                connection.setRequestProperty("Accept-Encoding", "lzf");
            }

            String body = request.getBody();
            if (StringUtils.isNotBlank(body)) {
                sendRequestBody(body, connection);
            }

            return new Response(response(connection), connection.getHeaderFields());
        } catch (ProtocolException e) {
            throw new LightblueHttpClientException(e);
        } catch (IOException e) {
            throw new LightblueHttpClientException(e);
        }
    }

    @Override
    public void close() throws IOException {
        // Nothing to do
    }

    private void sendRequestBody(String body, HttpURLConnection connection)
            throws IOException {
        byte[] bodyUtf8Bytes = body.getBytes(UTF_8);
        int length = bodyUtf8Bytes.length;

        connection.setDoOutput(true);
        connection.setFixedLengthStreamingMode(length);

        connection.setRequestProperty("Content-Length", Integer.toString(length));
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        try (OutputStream requestStream = connection.getOutputStream()) {
            requestStream.write(bodyUtf8Bytes);
        }
    }

    /**
     * Parses response, whether or not the request was successful, if possible.
     * Reads entire input stream and closes it so the socket knows it is
     * finished and may be put back into a pool for reuse.
     */
    private String response(HttpURLConnection connection) throws LightblueHttpClientException {
        try (InputStream responseStream = connection.getInputStream()) {
            return readResponseStream(responseStream, connection);
        } catch (IOException e) {
            try (InputStream errorResponseStream = connection.getErrorStream()) {
                String errorBody = null;
                if (errorResponseStream != null) {
                    errorBody = readResponseStream(errorResponseStream, connection);
                }

                if (errorBody != null && !errorBody.equals("")) {
                    // this may be a valid lightblue response containing errors
                    // but it can also be a standard html 500 or 404 from server
                    // we will know that when we start parsing
                    return errorBody;
                }

                throw new LightblueHttpClientException(e, connection.getResponseCode(), errorBody);
            } catch (IOException e1) {
                throw new LightblueHttpClientException(e);
            }
        }
    }

    /**
     * Tries to efficiently allocate the response string if the "Content-Length"
     * header is set.
     */
    private String readResponseStream(InputStream responseStream, HttpURLConnection connection)
            throws IOException {
        LOGGER.debug("Reading response stream");
        InputStream decodedStream = responseStream;
        if (compression == Compression.LZF && "lzf".equals(connection.getHeaderField("Content-Encoding"))) {
            LOGGER.debug("Decoding lzf");
            decodedStream = new LZFInputStream(responseStream);
        }

        int contentLength = connection.getContentLength();

        if (contentLength == 0) {
            return "";
        }

        if (contentLength > 0) {
            byte[] responseBytes = new byte[contentLength];
            IOUtils.readFully(decodedStream, responseBytes);
            return new String(responseBytes, UTF_8);
        }

        return IOUtils.toString(decodedStream, UTF_8);
    }

    public interface ConnectionFactory {
        HttpURLConnection openConnection(String uri) throws IOException;
    }

    public static class UrlConnectionFactory implements ConnectionFactory {
        @Override
        public HttpURLConnection openConnection(String uri) throws IOException {
            URL url = new URL(uri);
            return (HttpURLConnection) url.openConnection();
        }
    }

    public Compression getCompression() {
        return compression;
    }

    public void setCompression(Compression compression) {
        this.compression = compression;
    }
}

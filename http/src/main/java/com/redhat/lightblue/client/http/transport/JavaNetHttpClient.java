package com.redhat.lightblue.client.http.transport;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.auth.SslSocketFactories;
import com.redhat.lightblue.client.request.LightblueRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Objects;

/**
 * An {@link HttpClient} which uses vanilla java.net classes to make the request. Recommended to use
 * in a servlet environment which is already multi-threaded by virtue of the application server.
 * This class is thread safe, so you should use an application-scoped
 * {@link com.redhat.lightblue.client.http.LightblueHttpClient} backed by an instance of this class,
 * which is the default behavior if you have not passed in a different {@code HttpClient}.
 *
 * <p>This implementation takes advantage of HTTP persistent connections as per:
 * <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html">http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html</a>.
 * Sockets are left open to be reused after each request per Java SDK semantics.
 */
public class JavaNetHttpClient implements HttpClient {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final ConnectionFactory connectionFactory;
    private final SSLSocketFactory sslSocketFactory;

    public JavaNetHttpClient() {
        this((SSLSocketFactory) null);
    }

    public JavaNetHttpClient(ConnectionFactory connectionFactory) {
        this(connectionFactory, null);
    }
    public JavaNetHttpClient(SSLSocketFactory sslSocketFactory) {
        this(new UrlConnectionFactory(), sslSocketFactory);
    }

    /**
     * @param connectionFactory Injectable for testing. To actually make HTTP requests, use another
     *                          constructor or pass {@link UrlConnectionFactory}.
     * @param sslSocketFactory May be null, indicating {@link SSLSocketFactory#getDefault()}} will
     *                         be used.
     */
    public JavaNetHttpClient(ConnectionFactory connectionFactory, SSLSocketFactory sslSocketFactory) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "connectionFactory");
        this.sslSocketFactory = sslSocketFactory;
    }

    public static JavaNetHttpClient fromLightblueClientConfiguration(LightblueClientConfiguration config)
            throws GeneralSecurityException, IOException {
        Objects.requireNonNull(config, "config");

        SSLSocketFactory sslSocketFactory = config.useCertAuth()
                ? SslSocketFactories.javaNetSslSocketFactory(config)
                : null;

        return new JavaNetHttpClient(sslSocketFactory);
    }

    @Override
    public String executeRequest(LightblueRequest request, String baseUri) throws IOException {
        String url = request.getRestURI(baseUri);
        HttpURLConnection connection = connectionFactory.openConnection(url);

        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) connection;

            if (sslSocketFactory != null) {
                httpsUrlConnection.setSSLSocketFactory(sslSocketFactory);
            }
        }

        String body = request.getBody();
        if (StringUtils.isNotBlank(body)) {
            addRequestBodyToConnection(body, connection);
        }

        connection.setRequestMethod(request.getHttpMethod().toString());

        return response(connection);
    }

    @Override
    public void close() throws IOException {
        // Nothing to do
    }

    private void addRequestBodyToConnection(String body, HttpURLConnection connection)
            throws IOException {
        int length = body.length();

        connection.setDoOutput(true);
        connection.setFixedLengthStreamingMode(length);

        connection.setRequestProperty("Content-Length", Integer.toString(length));
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        try (OutputStream requestStream = connection.getOutputStream()) {
            requestStream.write(body.getBytes(UTF_8));
        }
    }

    /**
     * Parses response, whether or not the request was successful, if possible. Reads entire input
     * stream and closes in order to so the socket knows it is finished and may be put back into a
     * pool for reuse.
     */
    private String response(HttpURLConnection connection) throws IOException {
        try (InputStream responseStream = connection.getInputStream()) {
            return readResponseStream(responseStream, connection);
        } catch (IOException e) {
            try (InputStream errorResponseStream = connection.getErrorStream()) {
                if (errorResponseStream == null) {
                    // TODO: May want to start returning status code / error line.
                    // In the meantime I don't think lightblue should ever error without a response.
                    return "";
                }

                return readResponseStream(errorResponseStream, connection);
            }
        }
    }

    /**
     * Tries to efficiently allocate the response string if the "Content-Length" header is set.
     */
    private String readResponseStream(InputStream responseStream, HttpURLConnection connection)
            throws IOException {
        int contentLength = connection.getContentLength();

        if (contentLength == 0) {
            return "";
        }

        if (contentLength > 0) {
            byte[] responseBytes = new byte[contentLength];
            IOUtils.readFully(responseStream, responseBytes);
            return new String(responseBytes, UTF_8);
        }

        return IOUtils.toString(responseStream, UTF_8);
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
}

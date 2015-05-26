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
 * An {@link HttpTransport} which uses vanilla java.net classes to execute a
 * {@link LightblueRequest}. Recommended to use in a servlet environment which is already
 * multi-threaded by virtue of the application server. This class is thread safe, so you should use
 * an application-scoped {@link com.redhat.lightblue.client.http.LightblueHttpClient} backed by an
 * instance of this class, which is the default behavior if you have not passed in a different
 * {@code HttpTransport}.
 *
 * <p>This implementation takes advantage of HTTP persistent connections as per:
 * <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html">http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html</a>.
 * Sockets are left open to be reused after each request per Java SDK semantics.
 */
public class JavaNetHttpTransport implements HttpTransport {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final ConnectionFactory connectionFactory;
    private final SSLSocketFactory sslSocketFactory;

    public JavaNetHttpTransport() {
        this((SSLSocketFactory) null);
    }

    public JavaNetHttpTransport(ConnectionFactory connectionFactory) {
        this(connectionFactory, null);
    }
    public JavaNetHttpTransport(SSLSocketFactory sslSocketFactory) {
        this(new UrlConnectionFactory(), sslSocketFactory);
    }

    /**
     * @param connectionFactory Injectable for testing. To actually make HTTP requests, use another
     *                          constructor or pass {@link UrlConnectionFactory}.
     * @param sslSocketFactory May be null, indicating {@link SSLSocketFactory#getDefault()}} will
     *                         be used.
     */
    public JavaNetHttpTransport(ConnectionFactory connectionFactory, SSLSocketFactory sslSocketFactory) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "connectionFactory");
        this.sslSocketFactory = sslSocketFactory;
    }

    public static JavaNetHttpTransport fromLightblueClientConfiguration(LightblueClientConfiguration config)
            throws GeneralSecurityException, IOException {
        Objects.requireNonNull(config, "config");

        SSLSocketFactory sslSocketFactory = config.useCertAuth()
                ? SslSocketFactories.javaNetSslSocketFactory(config)
                : null;

        return new JavaNetHttpTransport(sslSocketFactory);
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

        connection.setRequestMethod(request.getHttpMethod().toString());
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Charset", "utf-8");

        String body = request.getBody();
        if (StringUtils.isNotBlank(body)) {
            sendRequestBody(body, connection);
        }

        return response(connection);
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
     * Parses response, whether or not the request was successful, if possible. Reads entire input
     * stream and closes it so the socket knows it is finished and may be put back into a pool for
     * reuse.
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

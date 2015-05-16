package com.redhat.lightblue.client.http.transport;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.HttpMethod;
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

public class JavaNetHttpClient implements HttpClient {
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
        HttpURLConnection connection = connectionFactory.openConnection(request.getRestURI(baseUri));

        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) connection;

            if (sslSocketFactory != null) {
                httpsUrlConnection.setSSLSocketFactory(sslSocketFactory);
            }
        }

        HttpMethod method = request.getHttpMethod();
        String body = request.getBody();
        int length = body.length();

        connection.setRequestMethod(method.toString());

        if (StringUtils.isNotBlank(body)) {
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(length);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(length));

            try (OutputStream requestStream = connection.getOutputStream()) {
                requestStream.write(body.getBytes(Charset.forName("UTF-8")));
            }
        }

        InputStream responseStream = connection.getInputStream();

        return IOUtils.toString(responseStream, Charset.forName("UTF-8"));
    }

    @Override
    public void close() throws IOException {
        // Do nothing
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

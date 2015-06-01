package com.redhat.lightblue.client.http.transport;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.auth.ApacheHttpClients;
import com.redhat.lightblue.client.request.LightblueRequest;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

public class LightblueApacheHttpTransport implements HttpTransport {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightblueApacheHttpTransport.class);

    private final CloseableHttpClient httpClient;

    public LightblueApacheHttpTransport(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static LightblueApacheHttpTransport fromLightblueClientConfiguration(LightblueClientConfiguration config)
            throws GeneralSecurityException, IOException {
        return new LightblueApacheHttpTransport(ApacheHttpClients.fromLightblueClientConfiguration(config));
    }

    @Override
    public String executeRequest(LightblueRequest request, String baseUri) throws IOException {
        HttpUriRequest httpOperation = makeHttpUriRequest(request, baseUri);

        LOGGER.debug("Calling " + httpOperation);

        if (LOGGER.isDebugEnabled()) {
            if (httpOperation instanceof HttpEntityEnclosingRequest) {
                LOGGER.debug("Request body: " + request.getBody());
            }
        }

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpOperation)) {
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, Consts.UTF_8);
        }
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    private HttpUriRequest makeHttpUriRequest(LightblueRequest request, String baseUri) throws UnsupportedEncodingException {
        String uri = request.getRestURI(baseUri);
        HttpUriRequest httpRequest = null;

        switch (request.getHttpMethod()) {
            case GET:
                httpRequest = new HttpGet(uri);
                break;
            case POST:
                httpRequest = new HttpPost(uri);
                break;
            case PUT:
                httpRequest = new HttpPost(uri);
                break;
            case DELETE:
                httpRequest = new HttpDelete(uri);
                break;
        }

        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = new StringEntity(request.getBody(), Consts.UTF_8);
            ((HttpEntityEnclosingRequest) httpRequest).setEntity(entity);

            httpRequest.setHeader("Content-Type", "application/json; charset=utf-8");
        }

        httpRequest.setHeader("Accept", "application/json");
        httpRequest.setHeader("Accept-Charset", "utf-8");

        return httpRequest;
    }
}

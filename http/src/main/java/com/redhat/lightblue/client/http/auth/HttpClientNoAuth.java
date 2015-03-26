/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.http.auth;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

/**
 * @deprecated Use {@link com.redhat.lightblue.client.http.auth.ApacheHttpClients} instead, or
 * create your own client using {@link org.apache.http.impl.client.HttpClients}. If you need an
 * {@link org.apache.http.conn.ssl.SSLConnectionSocketFactory}, see
 * {@link com.redhat.lightblue.client.http.auth.SslSocketFactories}.
 */
@Deprecated
public class HttpClientNoAuth implements HttpClientAuth {
    private final Registry<ConnectionSocketFactory> socketFactoryRegistry;

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientNoAuth.class);

    public HttpClientNoAuth() {
        SSLConnectionSocketFactory sslSocketFactory;

        try {
            sslSocketFactory = SslSocketFactories.defaultNoAuthSocketFactory();
        } catch (GeneralSecurityException e) {
            LOGGER.error("Error creating jks from certificates: ", e);
            throw new RuntimeException(e);
        }

        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
    }

    /* (non-Javadoc)
     * @see com.redhat.lightblue.client.http.LightblueHttpClientI#getClient()
     */
    @Override
    public CloseableHttpClient getClient() {
        HttpClientConnectionManager connManager;
        connManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
        return ApacheHttpClients.forConnectionManager(connManager);
    }
}

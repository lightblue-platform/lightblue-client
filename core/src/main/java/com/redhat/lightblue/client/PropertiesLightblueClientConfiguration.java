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
package com.redhat.lightblue.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides factory methods for
 * {@link com.redhat.lightblue.client.LightblueClientConfiguration} that is
 * derived from {@code .properties} files or {@link java.util.Properties}
 * objects.
 *
 * <p>
 * These properties files are read for specific keys for configuration:
 *
 * <dl>
 * <dt>dataServiceURI</dt>
 * <dd>The URL for the lightblue data service.</dd>
 * <dt>metadataServiceURI</dt>
 * <dd>The URL for the lightblue metadata service.</dd>
 * <dt>useCertAuth</dt>
 * <dd>Whether or not to use certificate authentication to talk to the lightblue
 * services.</dd>
 * <dt>caFilePath</dt>
 * <dd>SSL certificate for talking with lightblue services.</dd>
 * <dt>certFilePath</dt>
 * <dd>The file path to the client certificate. This follows the semantics of
 * {@link java.lang.ClassLoader#getResource(String)}, which is to say it is a
 * relative, / separated path from the root of the classpath, and should
 * <em>not</em> start with a forward slash.</dd>
 * <dt>certPassword</dt>
 * <dd>The password for the client certificate.</dd>
 * <dt>certAlias</dt>
 * <dd>The alias for the client certificate. ???</dd>
 * </dl>
 */
public final class PropertiesLightblueClientConfiguration {
    public static final String DEFAULT_CONFIG_FILE = "lightblue-client.properties";

    private static final String DATA_SERVICE_URI_KEY = "dataServiceURI";
    private static final String METADATA_SERVICE_URI_KEY = "metadataServiceURI";
    private static final String USE_CERT_AUTH_KEY = "useCertAuth";
    private static final String CA_FILE_PATH_KEY = "caFilePath";
    private static final String CERT_FILE_PATH_KEY = "certFilePath";
    private static final String CERT_PASSWORD_KEY = "certPassword";

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLightblueClientConfiguration.class);

    /**
     * Assumes a lightblue-client.properties file at the root of the classpath.
     *
     * <p>
     * For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     */
    public static LightblueClientConfiguration fromDefault() {
        return fromResource(DEFAULT_CONFIG_FILE);
    }

    /**
     * Returns a resource found using the current thread's context class loader.
     *
     * <p>
     * For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     *
     * @param resourcePath Follows the semantics of
     * {@link java.lang.ClassLoader#getResourceAsStream(String)}, which is to
     * say it is a relative / separated path from the root of the class path and
     * should <em>not</em> start with a forward slash (/).
     *
     * @see Thread#currentThread()
     * @see Thread#getContextClassLoader()
     */
    public static LightblueClientConfiguration fromResource(String resourcePath) {
        return fromResource(resourcePath, Thread.currentThread().getContextClassLoader());
    }

    /**
     * For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     *
     * @param resourcePath Follows the semantics of
     * {@link java.lang.ClassLoader#getResourceAsStream(String)}, which is to
     * say it is a relative / separated path from the root of the class path and
     * should <em>not</em> start with a forward slash (/).
     * @param classLoader The class loader to use to find the resource.
     */
    public static LightblueClientConfiguration fromResource(String resourcePath,
                                                            ClassLoader classLoader) {
        InputStream propertiesStream = classLoader.getResourceAsStream(resourcePath);

        if (propertiesStream == null) {
            LOGGER.error("Could not find properties resource at " + resourcePath);
            throw new LightblueClientConfigurationException("Could not find properties resource "
                    + "at " + resourcePath);
        }

        return fromInputStream(propertiesStream);
    }

    /**
     * For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     *
     * @param pathToProperties A file system path, relative to the working
     * directory of the java process.
     */
    public static LightblueClientConfiguration fromPath(Path pathToProperties) {
        try (InputStream inStream = Files.newInputStream(pathToProperties)) {
            return fromInputStream(inStream);
        } catch (IOException e) {
            LOGGER.error(pathToProperties + " could not be found/read", e);
            throw new LightblueClientConfigurationException("Could not read properties file from "
                    + "path, " + pathToProperties, e);
        }
    }

    /**
     * For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     */
    public static LightblueClientConfiguration fromInputStream(InputStream propertiesStream) {
        try {
            Properties properties = new Properties();
            properties.load(propertiesStream);

            return fromObject(properties);
        } catch (IOException e) {
            LOGGER.error(propertiesStream + " could not be read", e);
            throw new LightblueClientConfigurationException("Could not read properties file from "
                    + "input stream, " + propertiesStream, e);
        }
    }

    /**
     * For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     */
    public static LightblueClientConfiguration fromObject(Properties properties) {
        LightblueClientConfiguration config = new LightblueClientConfiguration();

        config.setCaFilePath(properties.getProperty(CA_FILE_PATH_KEY));
        config.setCertFilePath(properties.getProperty(CERT_FILE_PATH_KEY));
        config.setCertPassword(properties.getProperty(CERT_PASSWORD_KEY));
        config.setDataServiceURI(properties.getProperty(DATA_SERVICE_URI_KEY));
        config.setMetadataServiceURI(properties.getProperty(METADATA_SERVICE_URI_KEY));
        config.setUseCertAuth(Boolean.parseBoolean(properties.getProperty(USE_CERT_AUTH_KEY)));

        return config;
    }

    private PropertiesLightblueClientConfiguration() {
    }
}

package com.redhat.lightblue.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.lightblue.client.Execution.MongoController.ReadPreference;
import com.redhat.lightblue.client.LightblueClientConfiguration.Compression;

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
    private static final String COMPRESSION = "compression";
    private static final String READ_PREFERENCE = "readPreference";
    private static final String WRITE_CONCERN = "writeConcern";
    private static final String MAX_QUERY_TIME_MS = "maxQueryTimeMS";

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
            properties.load(loadInputStream(propertiesStream));

            return fromObject(properties);
        } catch (IOException e) {
            LOGGER.error(propertiesStream + " could not be read", e);
            throw new LightblueClientConfigurationException("Could not read properties file from "
                    + "input stream, " + propertiesStream, e);
        }
    }

    /**
     * Reads the {@link InputStream} and substitutes system properties.
     * @return {@link Reader}
     */
    private static Reader loadInputStream(InputStream propertiesStream) throws IOException {
        StringBuilder buff = new StringBuilder();

        try (InputStreamReader isr = new InputStreamReader(propertiesStream, Charset.defaultCharset());
                BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                buff.append(line).append("\n");
            }
        }

        return new StringReader(StrSubstitutor.replaceSystemProperties(buff.toString()));
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
        if (properties.containsKey(COMPRESSION)) {
            config.setCompression(Compression.parseCompression(properties.getProperty(COMPRESSION)));
        }
        if (properties.containsKey(READ_PREFERENCE)) {
            config.setReadPreference(ReadPreference.valueOf(properties.getProperty(READ_PREFERENCE)));
        }
        if (properties.containsKey(WRITE_CONCERN)) {
            config.setWriteConcern(properties.getProperty(WRITE_CONCERN));
        }
        if (properties.containsKey(MAX_QUERY_TIME_MS)) {
            config.setMaxQueryTimeMS(Integer.parseInt(properties.getProperty(MAX_QUERY_TIME_MS)));
        }
        
        return config;
    }

    private PropertiesLightblueClientConfiguration() {}
}

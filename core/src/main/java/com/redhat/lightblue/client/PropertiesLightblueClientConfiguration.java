package com.redhat.lightblue.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Lightblue client configuration that is originally derived from a properties file with predefined
 * keys, however it is mutable so it is not guaranteed that an instance of this class always has
 * the same properties as the property file.
 *
 * <dl>
 *     <dt>dataServiceURI</dt>
 *     <dd>The URL for the lightblue data service.</dd>
 *     <dt>metadataServiceURI</dt>
 *     <dd>The URL for the lightblue metadata service.</dd>
 *     <dt>useCertAuth</dt>
 *     <dd>Whether or not to use certificate authentication to talk to the lightblue services.</dd>
 *     <dt>caFilePath</dt>
 *     <dd>SSL certificate for talking with lightblue services.</dd>
 *     <dt>certFilePath</dt>
 *     <dd>The file path to the client certificate. This follows the semantics of
 *     {@link java.lang.ClassLoader#getResource(String)}, which is to say it is a relative, /
 *     separated path from the root of the classpath, and should <em>not</em> start with a forward
 *     slash.</dd>
 *     <dt>certPassword</dt>
 *     <dd>The password for the client certificate.</dd>
 *     <dt>certAlias</dt>
 *     <dd>The alias for the client certificate. ???</dd>
 * </dl>
 */
public class PropertiesLightblueClientConfiguration extends LightblueClientConfiguration {
    public static final String DEFAULT_CONFIG_FILE = "lightblue-client.properties";

    private static final String DATA_SERVICE_URI_KEY = "dataServiceURI";
    private static final String METADATA_SERVICE_URI_KEY = "metadataServiceURI";
    private static final String USE_CERT_AUTH_KEY = "useCertAuth";
    private static final String CA_FILE_PATH_KEY = "caFilePath";
    private static final String CERT_FILE_PATH_KEY = "certFilePath";
    private static final String CERT_PASSWORD_KEY = "certPassword";
    private static final String CERT_ALIAS_KEY = "certAlias";

    /**
     * Assumes a lightblue-client.properties file at the root of the classpath.
     *
     * <p> For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     */
    public PropertiesLightblueClientConfiguration() {
        this(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(DEFAULT_CONFIG_FILE));
    }

    /**
     * For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     */
    public PropertiesLightblueClientConfiguration(Path pathToProperties) {
        this(propertiesFromPath(pathToProperties));
    }

    /**
     * For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     */
    public PropertiesLightblueClientConfiguration(InputStream propertiesStream) {
        this(propertiesFromInputStream(propertiesStream));
    }

    /**
     * For client configuration property keys, see
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration}.
     */
    public PropertiesLightblueClientConfiguration(Properties properties) {
        setCaFilePath(properties.getProperty(CA_FILE_PATH_KEY));
        setCertAlias(properties.getProperty(CERT_ALIAS_KEY));
        setCertFilePath(properties.getProperty(CERT_FILE_PATH_KEY));
        setCertPassword(properties.getProperty(CERT_PASSWORD_KEY));
        setDataServiceURI(properties.getProperty(DATA_SERVICE_URI_KEY));
        setMetadataServiceURI(properties.getProperty(METADATA_SERVICE_URI_KEY));
        setUseCertAuth(Boolean.parseBoolean(properties.getProperty(USE_CERT_AUTH_KEY)));
    }

    private static Properties propertiesFromInputStream(InputStream propertiesStream) {
        try {
            Properties properties = new Properties();
            properties.load(propertiesStream);
            return properties;
        } catch (IOException e) {
            throw new LightblueClientConfigurationException("Could not read properties file from " +
                    "input stream, " + propertiesStream, e);
        }
    }

    private static Properties propertiesFromPath(Path pathToProperties) {
        try {
            return propertiesFromInputStream(Files.newInputStream(pathToProperties));
        } catch (IOException e) {
            throw new LightblueClientConfigurationException("Could not read properties file from " +
                    "path, " + pathToProperties, e);
        }
    }
}

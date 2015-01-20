package com.redhat.lightblue.client.http.servlet;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;

import javax.servlet.ServletContext;
import java.nio.file.Paths;

public class LightblueServletContextConfiguration {
    private static final String LIGHTBLUE_CLIENT_PROPERTIES_PATH_KEY = "lightblueClientPropertiesPath";

    private static final String DATA_SERVICE_URI_KEY = "dataServiceURI";
    private static final String METADATA_SERVICE_URI_KEY = "metadataServiceURI";
    private static final String USE_CERT_AUTH_KEY = "useCertAuth";
    private static final String CA_FILE_PATH_KEY = "caFilePath";
    private static final String CERT_FILE_PATH_KEY = "certFilePath";
    private static final String CERT_PASSWORD_KEY = "certPassword";
    private static final String MAX_CONNECTIONS_PER_ROUTE_KEY = "maxConnectionsPerRoute";
    private static final String MAX_CONNECTIONS_TOTAL_KEY = "maxConnectionsTotal";

    private static final String MAX_CONNECTIONS_TOTAL_DEFAULT = "100";
    private static final String MAX_CONNECTIONS_PER_ROUTE_DEFAULT = "20";

    private final ServletContext context;

    public LightblueServletContextConfiguration(ServletContext context) {
        this.context = context;
    }

    /**
     * Max connections per route is the maximum amount of concurrent http connections to be used
     * per route.
     *
     * <p>By default, determines the max connections per route from init parameters in the servlet
     * context. If none are provided, a default value is used as determined by
     * {@link #MAX_CONNECTIONS_PER_ROUTE_DEFAULT}.
     *
     * <p>See {@link #MAX_CONNECTIONS_PER_ROUTE_KEY} for the init parameter to set this value.
     *
     * <p>Example:
     * <pre><code>
     * {@code<web-app ... >}
     *     {@code<context-param>}
     *         {@code<param-name>}maxConnectionsPerRoute{@code</param-name>}
     *         {@code<param-value>}20{@code</param-value>}
     *      {@code</context-param>}
     * {@code</web-app>}
     * </code></pre>
     *
     * <p><em>Designed to be overridden if another configuration scheme is desired.</em> For
     * example, you could configure these settings with a properties file, or by writing your own
     * listener annotated with {@link javax.servlet.annotation.WebListener} that does not require a
     * deployment descriptor (web.xml) element.
     *
     * @see org.apache.http.impl.conn.PoolingHttpClientConnectionManager
     * @see org.apache.http.pool.ConnPoolControl
     */
    public int maxConnectionsPerRoute() {
        return parseInt(getInitParameterOrDefault(MAX_CONNECTIONS_PER_ROUTE_KEY,
                MAX_CONNECTIONS_PER_ROUTE_DEFAULT));
    }

    /**
     * Max connections total is the total number of concurrent http connections.
     *
     * <p>By default, determines the max connections total from init parameters in the servlet
     * context. If none are provided, a default value is used as determined by
     * {@link #MAX_CONNECTIONS_TOTAL_DEFAULT}.
     *
     * <p>See {@link #MAX_CONNECTIONS_TOTAL_KEY} for the init parameter to set this value.
     *
     * <p>Example:
     * <pre><code>
     * {@code<web-app ... >}
     *     {@code<context-param>}
     *         {@code<param-name>}maxConnectionsTotal{@code</param-name>}
     *         {@code<param-value>}100{@code</param-value>}
     *      {@code</context-param>}
     * {@code</web-app>}
     * </code></pre>
     *
     * <p><em>Designed to be overridden if another configuration scheme is desired.</em> For
     * example, you could configure these settings with a properties file, or by writing your own
     * listener annotated with {@link javax.servlet.annotation.WebListener} that does not require a
     * deployment descriptor (web.xml) element.
     *
     * @see org.apache.http.impl.conn.PoolingHttpClientConnectionManager
     * @see org.apache.http.pool.ConnPoolControl
     */
    public int maxConnectionsTotal() {
        return parseInt(getInitParameterOrDefault(MAX_CONNECTIONS_TOTAL_KEY,
                MAX_CONNECTIONS_TOTAL_DEFAULT));
    }

    /**
     * By default will use {@link #baseLightblueClientConfiguration()}. Following that, specific
     * parameters may be overridden as context parameters:
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
    public LightblueClientConfiguration lightblueClientConfiguration() {
        LightblueClientConfiguration configuration = baseLightblueClientConfiguration();

        String dataServiceUriParam = getInitParameter(DATA_SERVICE_URI_KEY);
        String metadataServiceUriParam = getInitParameter(METADATA_SERVICE_URI_KEY);
        String useCertAuthParam = getInitParameter(USE_CERT_AUTH_KEY);
        String caFilePathParam = getInitParameter(CA_FILE_PATH_KEY);
        String certFilePathParm = getInitParameter(CERT_FILE_PATH_KEY);
        String certPasswordParam = getInitParameter(CERT_PASSWORD_KEY);

        if (dataServiceUriParam != null) {
            configuration.setDataServiceURI(dataServiceUriParam);
        }

        if (metadataServiceUriParam != null) {
            configuration.setMetadataServiceURI(metadataServiceUriParam);
        }

        if (useCertAuthParam != null) {
            configuration.setUseCertAuth(parseBoolean(useCertAuthParam));
        }

        if (caFilePathParam != null) {
            configuration.setCaFilePath(caFilePathParam);
        }

        if (certFilePathParm != null) {
            configuration.setCertFilePath(certFilePathParm);
        }

        if (certPasswordParam != null) {
            configuration.setCertPassword(certPasswordParam);
        }

        return configuration;
    }

    /**
     * Returns
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration#fromDefault()}
     * by default if no context parameter with key {@link #LIGHTBLUE_CLIENT_PROPERTIES_PATH_KEY} is
     * specified. Otherwise, the path is used with
     * {@link com.redhat.lightblue.client.PropertiesLightblueClientConfiguration#fromPath(java.nio.file.Path)}.
     */
    protected LightblueClientConfiguration baseLightblueClientConfiguration() {
        String propertiesFilePath = context.getInitParameter(LIGHTBLUE_CLIENT_PROPERTIES_PATH_KEY);

        if (propertiesFilePath == null) {
            return PropertiesLightblueClientConfiguration.fromDefault();
        }

        return PropertiesLightblueClientConfiguration.fromPath(Paths.get(propertiesFilePath));
    }

    protected final String getInitParameter(String parameter) {
        return context.getInitParameter(parameter);
    }

    protected final String getInitParameterOrDefault(String parameter, String defaultValue) {
        String value = context == null ? null : getInitParameter(parameter);
        return value == null ? defaultValue : value;
    }
}

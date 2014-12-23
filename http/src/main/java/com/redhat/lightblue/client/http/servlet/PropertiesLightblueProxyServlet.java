package com.redhat.lightblue.client.http.servlet;

import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;

import java.nio.file.Paths;

public abstract class PropertiesLightblueProxyServlet extends AbstractLightblueProxyServlet {
    @Override
    protected LightblueClientConfiguration configuration() {
        String pathToProperties = getInitParameter("pathToProperties");

        if (pathToProperties == null) {
            return new PropertiesLightblueClientConfiguration();
        }

        return new PropertiesLightblueClientConfiguration(Paths.get(pathToProperties));
    }
}

package com.redhat.lightblue.client;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.lightblue.client.http.LightblueHttpClientCertAuth;
import com.redhat.lightblue.client.http.LightblueHttpClientNoAuth;
import com.redhat.lightblue.client.request.LightblueRequest;

public class LightblueClient {

	private String configFilePath = "clientconfig.properties";
	private String dataServiceURI;
	private String metadataServiceURI;
	private boolean useCertAuth = false;

	private static final Logger LOGGER = LoggerFactory.getLogger(LightblueClient.class);

	public LightblueClient() {
		try {
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream(configFilePath));
			metadataServiceURI = properties.getProperty("metadataServiceURI");
			dataServiceURI = properties.getProperty("dataServiceURI");
			if (metadataServiceURI == null && dataServiceURI == null) {
				throw new RuntimeException("Either metadataServiceURI or dataServiceURI must be defined in appconfig.properties");
			}
			useCertAuth = Boolean.parseBoolean(properties.getProperty("useCertAuth"));
		} catch (IOException io) {
			LOGGER.error("appconfig.properties could not be found/read", io);
			throw new RuntimeException(io);
		}
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public String metadata(LightblueRequest lightblueRequest) {
		LOGGER.debug("Calling metadata service with lightblueRequest: " + lightblueRequest.toString());
		return callService(lightblueRequest.getRestRequest(metadataServiceURI));
	}

	public String data(LightblueRequest lightblueRequest) {
		LOGGER.debug("Calling data service with lightblueRequest: " + lightblueRequest.toString());
		return callService(lightblueRequest.getRestRequest(dataServiceURI));
	}

	private String callService(HttpRequestBase httpOperation) {
		String jsonOut;
		try {
			try (CloseableHttpClient httpClient = getLightblueHttpClient()) {
				httpOperation.setHeader("Content-Type", "application/json");

				try (CloseableHttpResponse httpResponse = httpClient.execute(httpOperation)) {
					HttpEntity entity = httpResponse.getEntity();
					jsonOut = EntityUtils.toString(entity);
					LOGGER.debug("Response received from service" + jsonOut);
					return jsonOut;
				}
			}
		} catch (IOException e) {
			LOGGER.error("There was a problem calling the lightblue service", e);
			return "{\"error\":\"There was a problem calling the lightblue service\"}";
		}
	}

	private CloseableHttpClient getLightblueHttpClient() {
		if (useCertAuth) {
			LOGGER.debug("Using certificate authentication");
			return new LightblueHttpClientCertAuth().getClient();
		} else {
			LOGGER.debug("Using no authentication");
			return new LightblueHttpClientNoAuth().getClient();
		}
	}

}

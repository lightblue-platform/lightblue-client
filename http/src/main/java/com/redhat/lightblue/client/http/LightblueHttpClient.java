package com.redhat.lightblue.client.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueClientConfiguration;
import com.redhat.lightblue.client.http.auth.HttpClientCertAuth;
import com.redhat.lightblue.client.http.auth.HttpClientNoAuth;
import com.redhat.lightblue.client.http.request.LightblueHttpDataRequest;
import com.redhat.lightblue.client.http.request.LightblueHttpMetadataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.client.util.ClientConstants;

public class LightblueHttpClient implements LightblueClient {

	LightblueClientConfiguration configuration;
	
	private String dataServiceURI;
	private String metadataServiceURI;
	private boolean useCertAuth = false;
	private ObjectMapper mapper = new ObjectMapper();

	private static final Logger LOGGER = LoggerFactory.getLogger(LightblueHttpClient.class);

	/**
	 * This constructor will attempt to read the configuration from the default properties file on the classpath
	 */
	public LightblueHttpClient() {
		setObjectMapperDefaults();
		try {
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream(ClientConstants.DEFAULT_CONFIG_FILE));
			loadConfigFromProperties(properties);
		} catch (IOException io) {
			LOGGER.error(ClientConstants.DEFAULT_CONFIG_FILE + " could not be found/read", io);
			throw new RuntimeException(io);
		}
	}
	
	/**
	 * This constructor will attempt to read the configuration from the specified properties file on the file system
	 */
	public LightblueHttpClient(String configFilePath) {
		setObjectMapperDefaults();
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(configFilePath));
			loadConfigFromProperties(properties);
		} catch (IOException io) {
			LOGGER.error(configFilePath + " could not be found/read", io);
			throw new RuntimeException(io);
		}
	}

	/**
	 * This constructor will use the specified object and not attempt to read from a properties file at all 
	 * 
	 * @param configuration
	 */
	public LightblueHttpClient(LightblueClientConfiguration configuration) {
		setObjectMapperDefaults();
		metadataServiceURI = configuration.getMetadataServiceURI();
		dataServiceURI = configuration.getDataServiceURI();
		useCertAuth = configuration.useCertAuth();
	}
	
	private void setObjectMapperDefaults() {
		mapper.setDateFormat(ClientConstants.getDateFormat());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	private void loadConfigFromProperties(Properties properties) {
		metadataServiceURI = properties.getProperty("metadataServiceURI");
		dataServiceURI = properties.getProperty("dataServiceURI");
		if (metadataServiceURI == null && dataServiceURI == null) {
			throw new RuntimeException("Either metadataServiceURI or dataServiceURI must be defined in configuration");
		}
		useCertAuth = Boolean.parseBoolean(properties.getProperty("useCertAuth"));
	}
	
	/**
	 * @deprecated
	 * Use LightblueHttpClient(String configFilePath) if you want to specify a config file location not on the classpath
	 * Use LightblueHttpClient(LightblueClientConfiguration configuration) if you don't want to use config files at all
	 */
	public LightblueHttpClient(String dataServiceURI, String metadataServiceURI, Boolean useCertAuth) {
		mapper.setDateFormat(ClientConstants.getDateFormat());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.metadataServiceURI = metadataServiceURI;
		this.dataServiceURI = dataServiceURI;
		if (metadataServiceURI == null && dataServiceURI == null) {
			throw new RuntimeException("Either metadataServiceURI or dataServiceURI must be defined in appconfig.properties");
		}
		this.useCertAuth = useCertAuth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redhat.lightblue.client.LightblueClient#metadata(com.redhat.lightblue
	 * .client.request.LightblueRequest)
	 */
	@Override
	public LightblueResponse metadata(LightblueRequest lightblueRequest) {
		LOGGER.debug("Calling metadata service with lightblueRequest: " + lightblueRequest.toString());
		return callService(new LightblueHttpMetadataRequest(lightblueRequest).getRestRequest(metadataServiceURI));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redhat.lightblue.client.LightblueClient#data(com.redhat.lightblue.client
	 * .request.LightblueRequest)
	 */
	@Override
	public LightblueResponse data(LightblueRequest lightblueRequest) {
		LOGGER.debug("Calling data service with lightblueRequest: " + lightblueRequest.toString());
		return callService(new LightblueHttpDataRequest(lightblueRequest).getRestRequest(dataServiceURI));
	}

	public <T> T data(LightblueRequest lightblueRequest, Class<T> type) throws IOException {
		LightblueResponse response = data(lightblueRequest);

		JsonNode objectNode = response.getJson().path("processed");

		try {
			T object = mapper.readValue(objectNode.traverse(), type);

			return object;
		} catch (JsonMappingException e) {
			LOGGER.error("Error parsing lightblue response: " + response.getJson().toString(), e);
			throw new RuntimeException("Error parsing lightblue response: " + response.getJson().toString());
		}
	}

	protected LightblueResponse callService(HttpRequestBase httpOperation) {
		String jsonOut;

		LOGGER.debug("Calling " + httpOperation);
		try {
			try (CloseableHttpClient httpClient = getLightblueHttpClient()) {
				httpOperation.setHeader("Content-Type", "application/json");

				if (LOGGER.isDebugEnabled()) {
					try {
						LOGGER.debug("Request body: " + (EntityUtils.toString(((HttpEntityEnclosingRequestBase) httpOperation).getEntity())));
					} catch (ClassCastException e) {
						LOGGER.debug("Request body: None");
					}
				}

				try (CloseableHttpResponse httpResponse = httpClient.execute(httpOperation)) {
					HttpEntity entity = httpResponse.getEntity();
					jsonOut = EntityUtils.toString(entity);
					LOGGER.debug("Response received from service" + jsonOut);
					return new LightblueResponse(jsonOut);
				}
			}
		} catch (IOException e) {
			LOGGER.error("There was a problem calling the lightblue service", e);
			return new LightblueResponse("{\"error\":\"There was a problem calling the lightblue service\"}");
		}
	}

	private CloseableHttpClient getLightblueHttpClient() {
		CloseableHttpClient httpClient;
		if (useCertAuth) {
			LOGGER.debug("Using certificate authentication");
			if(configuration != null) {
				httpClient = new HttpClientCertAuth(configuration).getClient();
			} else 
				httpClient = new HttpClientCertAuth().getClient();
		} else {
			LOGGER.debug("Using no authentication");
			httpClient = new HttpClientNoAuth().getClient();
		}
		return httpClient;
	}

}

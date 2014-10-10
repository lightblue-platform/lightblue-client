package com.redhat.lightblue.client;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.lightblue.client.http.LightblueHttpClient;
import com.redhat.lightblue.client.http.LightblueHttpClientCertAuth;

public abstract class AbstractLightblueClient {

	private String serviceURI;
	private String metadataContextPath;
	private String dataContextPath;
	private boolean useCertAuth = true;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLightblueClient.class);

	public AbstractLightblueClient() {
		try {
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream("appconfig.properties"));
			serviceURI = properties.getProperty("serviceURI");
			if (serviceURI == null) {
				throw new RuntimeException("serviceURI must be defined in appconfig.properties");
			}
			metadataContextPath = properties.getProperty("metadataContextPath");
			dataContextPath = properties.getProperty("dataContextPath");
			useCertAuth = Boolean.parseBoolean(properties.getProperty("useCertAuth"));
		} catch (IOException io) {
			LOGGER.error("appconfig.properties could not be found/read", io);
			throw new RuntimeException(io);
		}
	}

	private String callService(HttpRequestBase httpOperation, String jsonIn) {
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
			return new LightblueHttpClient().getClient();
		}
	}
	
	public String getEntityMetadata() {	
		return callService(new HttpGet(serviceURI + metadataContextPath), null);
	}
	
	public String getEntityMetadata(String entityName) {	
		return callService(new HttpGet(serviceURI + metadataContextPath + entityName), null);
	}
	
	public String getEntityData(String jsonIn) {	
		return callService(new HttpGet(serviceURI + dataContextPath), jsonIn);
	}

	public String postEntityData(String jsonIn) {	
		return callService(new HttpPost(serviceURI + dataContextPath), jsonIn);
	}
	
	public String putEntityData(String jsonIn) {	
		return callService(new HttpPut(serviceURI + dataContextPath), jsonIn);
	}
	
	public String deleteEntityData(String jsonIn) {	
		return callService(new HttpDelete(serviceURI + dataContextPath), jsonIn);
	}
	
}

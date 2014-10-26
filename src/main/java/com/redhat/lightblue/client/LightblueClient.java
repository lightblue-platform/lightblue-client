package com.redhat.lightblue.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.http.LightblueHttpClient;
import com.redhat.lightblue.client.http.LightblueHttpClientCertAuth;
import com.redhat.lightblue.client.request.LightblueRequest;
import java.text.SimpleDateFormat;

public class LightblueClient {

	private String serviceURI;
	private String metadataContextPath;
	private String dataContextPath;
	private boolean useCertAuth = true;
    private ObjectMapper mapper = new ObjectMapper();

	private static final Logger LOGGER = LoggerFactory.getLogger(LightblueClient.class);
    public static final SimpleDateFormat lightblueDateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss.sssZ");

	public LightblueClient() {
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

	public String metadata(LightblueRequest lightblueRequest) {
		return callService(getRestRequest(lightblueRequest), lightblueRequest.getBody());
	}
	
	public String data(LightblueRequest lightblueRequest) {
		return callService(getRestRequest(lightblueRequest), lightblueRequest.getBody());
	}

    public <T> T data(LightblueRequest lightblueRequest, Class<T> type) throws IOException {
        String response = data(lightblueRequest);
        JsonNode rootNode = mapper.readTree(response);

        // TODO: handle lightblue errors

        JsonNode objectNode = rootNode.path("processed");

        T object = mapper.readValue(objectNode.traverse(), type);

        return object;
    }
	
	private String getRestURI(LightblueRequest lightblueRequest) {
		StringBuilder requestURI = new StringBuilder();
		
		for (RequestType requestType : RequestType.values()) {
			if(requestType.equals(lightblueRequest.getRequestType())) {
				requestURI.append(serviceURI + requestType.getValue());
			}
		}
		
		if(StringUtils.isNotBlank(lightblueRequest.getEntityName())) {
			requestURI.append(lightblueRequest.getEntityName() + "/");
		}
		
		if(StringUtils.isNotBlank(lightblueRequest.getEntityVersion())) {
			requestURI.append(lightblueRequest.getEntityVersion() + "/");
		}
		
		return requestURI.toString();
	}
	
	private HttpRequestBase getRestRequest(LightblueRequest lightblueRequest) {
		HttpRequestBase httpOperation;

		try {
			if (RequestType.DATA_INSERT.equals(lightblueRequest.getRequestType())) {
				HttpPut httpPut = new HttpPut(getRestURI(lightblueRequest));
				httpPut.setEntity(new StringEntity(lightblueRequest.getBody()));
				httpOperation = httpPut;
			} else if (RequestType.DATA_FIND.equals(lightblueRequest.getRequestType())) {
				HttpPost httpPost = new HttpPost(getRestURI(lightblueRequest));
				httpPost.setEntity(new StringEntity(lightblueRequest.getBody()));
				httpOperation = httpPost;
			} else if (RequestType.DATA_DELETE.equals(lightblueRequest.getRequestType())) {
				HttpPost httpPost = new HttpPost(getRestURI(lightblueRequest));
				httpPost.setEntity(new StringEntity(lightblueRequest.getBody()));
				httpOperation = httpPost;
			} else if (RequestType.DATA_UPDATE.equals(lightblueRequest.getRequestType())) {
				HttpPost httpPost = new HttpPost(getRestURI(lightblueRequest));
				httpPost.setEntity(new StringEntity(lightblueRequest.getBody()));
				httpOperation = httpPost;
			} else if (RequestType.DATA_SAVE.equals(lightblueRequest.getRequestType())) {
				HttpPost httpPost = new HttpPost(getRestURI(lightblueRequest));
				httpPost.setEntity(new StringEntity(lightblueRequest.getBody()));
				httpOperation = httpPost;
			} else if (RequestType.METADATA.equals(lightblueRequest.getRequestType())) {
				httpOperation = new HttpGet(getRestURI(lightblueRequest));
			} else {
				httpOperation = new HttpGet(getRestURI(lightblueRequest));
			}	
		} catch(UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}
				
		return httpOperation;
	}
	
}

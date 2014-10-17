package com.redhat.lightblue.client.request;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

public abstract class AbstractLightblueRequest implements LightblueRequest {

	static String PATH_SEPARATOR = "/";
	protected static String PATH_PARAM_ENTITY = "entity";
	protected static String PATH_PARAM_VERSION = "version";
	
	private String entityName;
	private String entityVersion;
	private String body;
	
	@Override
	public String getEntityName() {
		return entityName;
	}

	@Override
	public String getEntityVersion() {
		return entityVersion;
	}

	@Override
	public String getBody() {
		return body;
	}
	
	@Override
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@Override
	public void setEntityVersion(String entityVersion) {
		this.entityVersion = entityVersion;
	}
	
	@Override
	public void setBody(String body) {
		this.body = body;
	}
	

	
	protected void appendToURI(StringBuilder restOfURI, String pathParam) {
		if(!StringUtils.endsWith(restOfURI.toString(), PATH_SEPARATOR)) {
			restOfURI.append(PATH_SEPARATOR);
		} 
		restOfURI.append(pathParam);
	}
	
  protected HttpPost getHttpPost(String uri, String body) {
  	HttpPost httpPost = new HttpPost(uri);
		try {
	    httpPost.setEntity(new StringEntity(body));
    } catch (UnsupportedEncodingException e) {
	    throw new RuntimeException(e);
    }
		return httpPost;
  }
  
  protected HttpPut getHttpPut(String uri, String body) {
  	HttpPut httpPut = new HttpPut(uri);
		try {
	    httpPut.setEntity(new StringEntity(body));
    } catch (UnsupportedEncodingException e) {
	    throw new RuntimeException(e);
    }
		return httpPut;
  }
  
  protected HttpDelete getHttpDelete(String uri) {
		return new HttpDelete(uri);
  }
  
  protected HttpGet getHttpGet(String uri) {
		return new HttpGet(uri);
  }

}

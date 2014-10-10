package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;

public class AbstractLightblueRequest implements LightblueRequest {

	private String entityName;
	private String entityVersion;
	private String body;
	
	@Override
	public RequestType getRequestType() {
		return null;
	}

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

}

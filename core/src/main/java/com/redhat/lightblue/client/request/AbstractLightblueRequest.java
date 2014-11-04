package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

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

}

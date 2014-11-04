package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;


public abstract class AbstractLightblueMetadataRequest extends AbstractLightblueRequest implements LightblueRequest {

	public static String PATH_PARAM_GET_ENTITY_NAMES = "";
	public static String PATH_PARAM_GET_ENTITY_VERSIONS = "";
	public static String PATH_PARAM_GET_ENTITY_METADATA = "";
	public static String PATH_PARAM_GET_ENTITY_DEPENDENCIES = "dependencies";
	public static String PATH_PARAM_GET_ENTITY_ROLES = "roles";
	public static String PATH_PARAM_CREATE_METADATA = "";
	public static String PATH_PARAM_CREATE_SCHEMA = "";
	public static String PATH_PARAM_UPDATE_SCHEMA_STATUS = "";
	public static String PATH_PARAM_UPDATE_ENTITY_INFO = "";
	public static String PATH_PARAM_SET_DEFAULT_VERSION = "default";
	public static String PATH_PARAM_REMOVE_ENTITY = "";
	public static String PATH_PARAM_CLEAR_DEFAULT_VERSION = "";
	
	@Override
	public String getRestURI(String baseServiceURI) {
		StringBuilder requestURI = new StringBuilder();
			
		requestURI.append(baseServiceURI);
		
		if (StringUtils.isNotBlank(this.getEntityName())) {
			appendToURI(requestURI, this.getEntityName());
		}

		if (StringUtils.isNotBlank(this.getEntityVersion())) {
			appendToURI(requestURI, this.getEntityVersion());
		}
		
		if (StringUtils.isNotBlank(this.getOperationPathParam())) {
			appendToURI(requestURI, getOperationPathParam());
		}

		return requestURI.toString();
	}
}

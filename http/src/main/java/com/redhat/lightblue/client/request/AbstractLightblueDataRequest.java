package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractLightblueDataRequest extends AbstractLightblueRequest implements LightblueRequest {

	protected static String CONTEXT_PATH = "rest/data";
	protected static String PATH_PARAM_INSERT = "";
	protected static String PATH_PARAM_SAVE = "save";
	protected static String PATH_PARAM_UPDATE = "update";
	protected static String PATH_PARAM_DELETE = "delete";
	protected static String PATH_PARAM_FIND = "find";
	
	@Override
	public String getRestURI(String baseServiceURI) {
		StringBuilder requestURI = new StringBuilder();
			
		requestURI.append(baseServiceURI);
		appendToURI(requestURI, CONTEXT_PATH);
		
		appendToURI(requestURI, getOperationPathParam());
		
		if (StringUtils.isNotBlank(this.getEntityName())) {
			requestURI.append(this.getEntityName() + "/");
		}

		if (StringUtils.isNotBlank(this.getEntityVersion())) {
			requestURI.append(this.getEntityVersion() + "/");
		}
		return requestURI.toString();
	}
	
}
 
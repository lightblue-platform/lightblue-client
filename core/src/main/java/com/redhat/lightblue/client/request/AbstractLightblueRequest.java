package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractLightblueRequest implements LightblueRequest {

    protected static final String PATH_SEPARATOR = "/";
    protected static final String QUERY_SEPARATOR = "&";
	protected static final String QUERY_BEGINNER = "?";
	protected static final String QUERY_PARAM_NAME_VALUE_SEPERATOR = "=";

    private final String entityName;
    private final String entityVersion;

    /**
     * Construct request with entity name and default version
     */
    public AbstractLightblueRequest(String entityName) {
        this(entityName, null);
    }

    /**
     * Construct request with entity name and given version
     */
    public AbstractLightblueRequest(String entityName, String version) {
        this.entityName=entityName;
        entityVersion=version;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public static void appendToURI(StringBuilder restOfURI, String pathParam) {
        if (!StringUtils.endsWith(restOfURI.toString(), PATH_SEPARATOR)) {
            restOfURI.append(PATH_SEPARATOR);
        }
        restOfURI.append(pathParam);
    }

	protected void appendToURI(StringBuilder restOfURI, String queryParamName, String queryParamvalue) {
		if (!StringUtils.endsWith(restOfURI.toString(), PATH_SEPARATOR)) {
			if (!StringUtils.contains(restOfURI.toString(), QUERY_PARAM_NAME_VALUE_SEPERATOR)) {
				restOfURI.append(QUERY_BEGINNER);
			} else {
				restOfURI.append(QUERY_SEPARATOR);
			}
			restOfURI.append(queryParamName);
			restOfURI.append(QUERY_PARAM_NAME_VALUE_SEPERATOR);
			restOfURI.append(queryParamvalue);
		}

	}


    @Override
    public String toString() {
        return getHttpMethod()+" "+getRestURI("/")+", body: "+getBody();
    }
}

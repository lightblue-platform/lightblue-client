package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractLightblueRequest implements LightblueRequest {

    protected static final String PATH_SEPARATOR = "/";

    private String entityName;
    private String entityVersion;

    public AbstractLightblueRequest() {}

    /**
     * Construct request with entity name and default version
     */
    public AbstractLightblueRequest(String entityName) {
        this(entityName,null);
    }

    /**
     * Construct request with entity name and given version
     */
    public AbstractLightblueRequest(String entityName, String version) {
        this.entityName=entityName;
        this.entityVersion=version;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }


    protected void appendToURI(StringBuilder restOfURI, String pathParam) {
        if (!StringUtils.endsWith(restOfURI.toString(), PATH_SEPARATOR)) {
            restOfURI.append(PATH_SEPARATOR);
        }
        restOfURI.append(pathParam);
    }

    @Override
    public String toString() {
        return getHttpMethod().toString()+" "+getRestURI("/")+", body: "+getBody();
    }
}

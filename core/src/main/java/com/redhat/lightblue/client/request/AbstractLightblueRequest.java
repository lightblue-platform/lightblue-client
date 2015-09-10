package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.node.ContainerNode;

import com.redhat.lightblue.client.util.JSON;

public abstract class AbstractLightblueRequest implements LightblueRequest {

    protected static final String PATH_SEPARATOR = "/";
    protected static final String QUERY_SEPARATOR = "&";
	protected static final String QUERY_BEGINNER = "?";
	protected static final String QUERY_PARAM_NAME_VALUE_SEPERATOR = "=";

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
        return getHttpMethod().toString()+" "+getRestURI("/")+", body: "+getBody();
    }

    /**
     * Deprecated expression model support
     */
    protected com.redhat.lightblue.client.Query toq(com.redhat.lightblue.client.expression.query.Query q) {
        return com.redhat.lightblue.client.Query.query((ContainerNode)JSON.toJsonNode(q.toJson()));
    }

    /**
     * Deprecated expression model support
     */
    protected com.redhat.lightblue.client.Projection top(com.redhat.lightblue.client.projection.Projection p) {
        return com.redhat.lightblue.client.Projection.project((ContainerNode)JSON.toJsonNode(p.toJson()));
    }
    
    /**
     * Deprecated expression model support
     */
    protected com.redhat.lightblue.client.Sort tos(com.redhat.lightblue.client.request.SortCondition s) {
        return com.redhat.lightblue.client.Sort.sort((ContainerNode)JSON.toJsonNode(s.toJson()));
    }

    /**
     * Deprecated expression model support
     */
    protected com.redhat.lightblue.client.Update tou(com.redhat.lightblue.client.expression.update.Update u) {
        return com.redhat.lightblue.client.Update.update((ContainerNode)JSON.toJsonNode(u.toJson()));
    }
}

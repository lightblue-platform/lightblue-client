package com.redhat.lightblue.client.request;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import com.redhat.lightblue.client.http.HttpMethod;

/**
 * Encapsulates an HTTP request to be used with a Lightblue HTTP service. This
 * means that the body, if present, will be UTF-8 JSON.
 */
public abstract class LightblueRequest implements Serializable {

    private static final long serialVersionUID=1l;

    protected static final String PATH_SEPARATOR = "/";
    protected static final String QUERY_SEPARATOR = "&";
    protected static final String QUERY_BEGINNER = "?";
    protected static final String QUERY_PARAM_NAME_VALUE_SEPERATOR = "=";

    protected final HttpMethod httpMethod;

    public LightblueRequest(HttpMethod method) {
        this.httpMethod=method;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
    
    /**
     * Return request body JSON document. Return null if there is no body
     */
    public abstract JsonNode getBodyJson();

    /**
     * Requst body as string, defaults to getBodyJson().toString()
     */
    @Deprecated
    public String getBody() {
        JsonNode body=getBodyJson();
        if(body!=null) {
            return body.toString();
        } else {
            return null;
        }
    }

    /**
     * Request URI, appended to the baseServiceURI
     */
    public abstract String getRestURI(String baseServiceURI);


    public static void appendToURI(StringBuilder restOfURI, String pathParam) {
        if (!StringUtils.endsWith(restOfURI.toString(), PATH_SEPARATOR)) {
            restOfURI.append(PATH_SEPARATOR);
        }
        restOfURI.append(pathParam);
    }

    public static void appendToURI(StringBuilder restOfURI, String queryParamName, String queryParamvalue) {
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
        return getHttpMethod()+" "+getRestURI("")+", body: "+getBody();
    }
    
}

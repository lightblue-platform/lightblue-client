package com.redhat.lightblue.client.request.data;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueRequest;

/**
 * Generate a value using a field's value generator
 *
 * path: Path for the field whose value generator will be invoked n: Number of
 * values to generate, defaults to 1
 *
 * Parse the results as:
 * <pre>
 *	String[] results = client.data(request, String[].class);
 * </pre>
 *
 */
public class GenerateRequest extends AbstractLightblueRequest {

    private Integer n;
    private String path;

    public GenerateRequest(String entityName, String entityVersion, String path, int n) {
        super(entityName, entityVersion);
        this.path = path;
        this.n = n;
    }

    public GenerateRequest(String entityName, String entityVersion, String path) {
        super(entityName, entityVersion);
        this.path = path;
    }

    public GenerateRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public GenerateRequest(String entityName) {
        super(entityName);
    }

    /**
     * The path for the field whole value generator will be invoked
     */
    public GenerateRequest path(String path) {
        this.path = path;
        return this;
    }

    /**
     * Number of values to generate
     */
    public GenerateRequest nValues(int n) {
        this.n = n;
        return this;
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        if(path==null)
            throw new NullPointerException("path");
        StringBuilder bld=new StringBuilder();
        bld.append(baseServiceURI);
        appendToURI(bld, "generate");
        appendToURI(bld, getEntityName());
        if (StringUtils.isNotBlank(getEntityVersion())) {
            appendToURI(bld, getEntityVersion());
        }
        appendToURI(bld,path);
        if(n!=null)
            appendToURI(bld,"n",n.toString());
        return bld.toString();
    }

    @Override
    public JsonNode getBodyJson() {
        return null;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}

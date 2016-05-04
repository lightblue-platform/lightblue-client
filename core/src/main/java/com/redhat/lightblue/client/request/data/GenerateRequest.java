package com.redhat.lightblue.client.request.data;


import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.Sort;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

/**
 * Generate a value using a field's value generator
 *
 * path: Path for the field whose value generator will be invoked
 * n: Number of values to generate, defaults to 1
 *
 * Parse the results as:
 * <pre>
 *	String[] results = client.data(request, String[].class);
 * </pre>
 * 
 */
public class GenerateRequest extends AbstractLightblueDataRequest {

    private Integer n;
    private String path;

    public GenerateRequest(String entityName, String entityVersion,String path,int n) {
        super(entityName, entityVersion);
        this.path=path;
        this.n=n;
    }

    public GenerateRequest(String entityName, String entityVersion,String path) {
        super(entityName, entityVersion);
        this.path=path;
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
        this.path=path;
        return this;
    }

    /**
     * Number of values to generate
     */
    public GenerateRequest nValues(int n) {
        this.n=n;
        return this;
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        StringBuilder bld=new StringBuilder();
        bld.append(super.getRestURI(baseServiceURI));
        if(path==null)
            throw new NullPointerException("path");
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
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getOperationPathParam() {
        return "generate";
    }

    @Override
    public Operation getOperation() {
        return null;
    }
}

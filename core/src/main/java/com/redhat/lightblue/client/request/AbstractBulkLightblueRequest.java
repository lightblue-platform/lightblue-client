/**
 * 
 */
package com.redhat.lightblue.client.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;

/**
 * @author bvulaj
 *
 */
public abstract class AbstractBulkLightblueRequest<E extends AbstractLightblueRequest> implements LightblueRequest {

    protected List<E> requests;

    public AbstractBulkLightblueRequest() {
        this.requests = new ArrayList<E>();
    }

    /**
     * Adds a request to the end of the current request chain.
     * 
     * @param request
     * @return
     */
    public AbstractBulkLightblueRequest<E> add(E request) {
        this.requests.add(request);
        return this;
    }

    /**
     * Adds a collection of requests to the end of the current request chain.
     * 
     * @param requests
     * @return 
     */
    public AbstractBulkLightblueRequest<E> addAll(List<E> requests) {
        this.requests.addAll(requests);
        return this;
    }

    /**
     * Inserts a request at the given index. This method should not be preferred over the before / after methods.
     * 
     * @param request
     * @param index
     * @return 
     */
    public AbstractBulkLightblueRequest<E> insert(E request, int index) {
        this.requests.add(index, request);
        return this;
    }

    /**
     * Inserts a request before another specified request. This guarantees that the first request parameter will be executed, sequentially, before the second request parameter. It
     * does not guarantee consecutive execution.
     * 
     * @param request
     * @param before
     * @return 
     */
    public AbstractBulkLightblueRequest<E> insertBefore(E request, E before) {
        this.requests.add(requests.indexOf(before), request);
        return this;
    }

    /**
     * Inserts a request after another specified request. This guarantees that the first request parameter will be executed, sequentially, after the second request parameter. It
     * does not guarantee consecutive execution.
     * 
     * @param request
     * @param after
     * @return 
     */
    public AbstractBulkLightblueRequest<E> insertAfter(E request, E after) {
        this.requests.add(requests.indexOf(after) + 1, request);
        return this;
    }

    @Override
    public abstract JsonNode getBodyJson();

    @Override
    public String getBody() {
        return getBodyJson().toString();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        StringBuilder requestURI = new StringBuilder();
        requestURI.append(baseServiceURI);
        requestURI.append("/bulk");
        return requestURI.toString();
    }

    public List<E> getRequests() {
        return this.requests;
    }

    public void setRequests(List<E> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return getHttpMethod().toString() + " " + getRestURI("/") + ", body: " + getBody();
    }

}

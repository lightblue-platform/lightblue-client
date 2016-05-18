/**
 *
 */
package com.redhat.lightblue.client.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;

/**
 * @author bvulaj
 *
 */
public abstract class AbstractDataBulkRequest<E extends AbstractLightblueRequest> implements LightblueRequest {

    protected List<E> requests;

    public AbstractDataBulkRequest() {
        this.requests = new ArrayList<E>();
    }

    /**
     * Adds a request to the end of the current request chain.
     *
     * @param request
     * @return
     */
    public AbstractDataBulkRequest<E> add(E request) {
        this.requests.add(request);
        return this;
    }

    /**
     * Adds a collection of requests to the end of the current request chain in
     * iteration order.
     *
     * <p>
     * As the order of responses depends on the order of requests, use a
     * collection with a reliable iteration order if you need to get a specific
     * response for a specific request by index. Otherwise, you can also
     * retrieve the response for a specific request using
     * {@link com.redhat.lightblue.client.response.LightblueBulkDataResponse#getResponse(LightblueRequest)}.
     *
     * @param requests
     * @return
     */
    public AbstractDataBulkRequest<E> addAll(Collection<? extends E> requests) {
        this.requests.addAll(requests);
        return this;
    }

    /**
     * Inserts a request at the given index. This method should not be preferred
     * over the before / after methods.
     *
     * @param request
     * @param index
     * @return
     */
    public AbstractDataBulkRequest<E> insert(E request, int index) {
        this.requests.add(index, request);
        return this;
    }

    /**
     * Inserts a request before another specified request. This guarantees that
     * the first request parameter will be executed, sequentially, before the
     * second request parameter. It does not guarantee consecutive execution.
     *
     * @param request
     * @param before
     * @return
     */
    public AbstractDataBulkRequest<E> insertBefore(E request, E before) {
        this.requests.add(requests.indexOf(before), request);
        return this;
    }

    /**
     * Inserts a request after another specified request. This guarantees that
     * the first request parameter will be executed, sequentially, after the
     * second request parameter. It does not guarantee consecutive execution.
     *
     * @param request
     * @param after
     * @return
     */
    public AbstractDataBulkRequest<E> insertAfter(E request, E after) {
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
        AbstractLightblueRequest.appendToURI(requestURI, "bulk");
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

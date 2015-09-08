/**
 * 
 */
package com.redhat.lightblue.client.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;

/**
 * @author bvulaj
 *
 */
public abstract class AbstractBulkLightblueRequest<E extends AbstractLightblueRequest> implements LightblueRequest {

	protected List<E> requests = new ArrayList<E>();

	public AbstractBulkLightblueRequest() {

	}

	public AbstractBulkLightblueRequest(List<E> requests) {
		this.requests.addAll(requests);
	}

	/**
	 * Adds a request to the end of the current request chain.
	 * 
	 * @param request
	 */
	public void add(E request) {
		requests.add(request);
	}

	/**
	 * Adds a collection of requests to the end of the current request chain.
	 * 
	 * @param requests
	 */
	public void addAll(List<E> requests) {
		requests.addAll(requests);
	}

	/**
	 * Inserts a request at the given index. This method should not be preferred over the before / after methods.
	 * 
	 * @param request
	 * @param index
	 */
	public void insert(E request, int index) {
		requests.add(index, request);
	}

	/**
	 * Inserts a request before another specified request. This guarantees that the first request parameter will be executed, sequentially, before the second request parameter. It
	 * does not guarantee consecutive execution.
	 * 
	 * @param request
	 * @param before
	 */
	public void insertBefore(E request, E before) {
		requests.add(requests.indexOf(before), request);
	}

	/**
	 * Inserts a request after another specified request. This guarantees that the first request parameter will be executed, sequentially, after the second request parameter. It
	 * does not guarantee consecutive execution.
	 * 
	 * @param request
	 * @param after
	 */
	public void insertAfter(E request, E after) {
		requests.add(requests.indexOf(after) + 1, request);
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
		return requests;
	}

	public void setRequests(List<E> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return getHttpMethod().toString() + " " + getRestURI("/") + ", body: " + getBody();
	}

}

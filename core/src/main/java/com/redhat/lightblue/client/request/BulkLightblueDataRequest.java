/**
 * 
 */
package com.redhat.lightblue.client.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.request.data.DataSaveRequest;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;
import com.redhat.lightblue.client.util.JSON;

/**
 * @author bvulaj
 *
 */
public class BulkLightblueDataRequest implements LightblueRequest {

	private List<AbstractLightblueDataRequest> reqs = new ArrayList<AbstractLightblueDataRequest>();

	public BulkLightblueDataRequest() {

	}

	public BulkLightblueDataRequest(List<AbstractLightblueDataRequest> aldrs) {
		this.reqs.addAll(aldrs);
	}

	/**
	 * Adds a request to the end of the current request chain.
	 * 
	 * @param aldr
	 */
	public void add(AbstractLightblueDataRequest aldr) {
		reqs.add(aldr);
	}

	/**
	 * Adds a collection of requests to the end of the current request chain.
	 * 
	 * @param aldrs
	 */
	public void addAll(List<AbstractLightblueDataRequest> aldrs) {
		reqs.addAll(aldrs);
	}

	/**
	 * Inserts a request at the given index. This method should not be preferred over the before / after methods.
	 * 
	 * @param aldr
	 * @param index
	 */
	public void insert(AbstractLightblueDataRequest aldr, int index) {
		reqs.add(index, aldr);
	}

	/**
	 * Inserts a request before another specified request. This guarantees that the first request parameter will be executed, sequentially, before the second request parameter. It
	 * does not guarantee consecutive execution.
	 * 
	 * @param aldr
	 * @param before
	 */
	public void insertBefore(AbstractLightblueDataRequest aldr, AbstractLightblueDataRequest before) {
		reqs.add(reqs.indexOf(before), aldr);
	}

	/**
	 * Inserts a request after another specified request. This guarantees that the first request parameter will be executed, sequentially, after the second request parameter. It
	 * does not guarantee consecutive execution.
	 * 
	 * @param aldr
	 * @param after
	 */
	public void insertAfter(AbstractLightblueDataRequest aldr, AbstractLightblueDataRequest after) {
		reqs.add(reqs.indexOf(after) + 1, aldr);
	}

	@Override
	public JsonNode getBodyJson() {
		ArrayNode node = JsonNodeFactory.instance.arrayNode();
		for (AbstractLightblueDataRequest req : reqs) {
			ObjectNode seqNode = JsonNodeFactory.instance.objectNode();
			seqNode.set("seq", JSON.toJsonNode(node.size()));
			String op = "";
			if (req instanceof DataDeleteRequest) {
				op = "delete";
			} else if (req instanceof DataFindRequest) {
				op = "find";
			} else if (req instanceof DataInsertRequest) {
				op = "insert";
			} else if (req instanceof DataSaveRequest) {
				op = "save";
			} else if (req instanceof DataUpdateRequest) {
				op = "update";
			}
			seqNode.set("op", JSON.toJsonNode(op));
			seqNode.set("request", req.getBodyJson());
			node.add(seqNode);
		}
		return node;
	}

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
}

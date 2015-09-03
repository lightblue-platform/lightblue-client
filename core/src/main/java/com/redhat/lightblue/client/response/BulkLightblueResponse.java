/**
 * 
 */
package com.redhat.lightblue.client.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;
import com.redhat.lightblue.client.request.AbstractBulkLightblueRequest;
import com.redhat.lightblue.client.request.AbstractLightblueRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.util.JSON;

/**
 * @author bvulaj
 *
 */
public class BulkLightblueResponse implements LightblueResponse {

	private List<LightblueResponse> resps = new ArrayList<LightblueResponse>();
	protected List<AbstractBulkLightblueRequest> reqs = new ArrayList<AbstractBulkLightblueRequest>();

	public BulkLightblueResponse() {
	}

	public BulkLightblueResponse(List<LightblueResponse> resps, List<AbstractBulkLightblueRequest> reqs) {
		this.resps = resps;
		this.reqs = reqs;
	}

	@Override
	public String getText() {
		return getJson().toString();
	}

	@Override
	public JsonNode getJson() {
		ArrayNode node = JsonNodeFactory.instance.arrayNode();
		for (LightblueResponse resp : resps) {
			ObjectNode seqNode = JsonNodeFactory.instance.objectNode();
			seqNode.set("seq", JSON.toJsonNode(resps.indexOf(resp)));
			seqNode.set("response", resp.getJson());
			node.add(seqNode);
		}
		return node;
	}

	@Override
	public boolean hasError() {
		for (LightblueResponse resp : resps) {
			if (resp.hasError()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasDataErrors() {
		for (LightblueResponse resp : resps) {
			if (resp.hasDataErrors()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Error[] getErrors() {
		List<Error> errors = new ArrayList<Error>();
		for (LightblueResponse resp : resps) {
			errors.addAll(Arrays.asList(resp.getErrors()));
		}
		return (Error[]) errors.toArray();
	}

	@Override
	public DataError[] getDataErrors() {
		List<DataError> errors = new ArrayList<DataError>();
		for (LightblueResponse resp : resps) {
			errors.addAll(Arrays.asList(resp.getDataErrors()));
		}
		return (DataError[]) errors.toArray();
	}

	@Override
	public int parseModifiedCount() {
		int mod = 0;
		for (LightblueResponse resp : resps) {
			mod += resp.parseModifiedCount();
		}
		return mod;
	}

	@Override
	public int parseMatchCount() {
		int match = 0;
		for (LightblueResponse resp : resps) {
			match += resp.parseMatchCount();
		}
		return match;
	}

	@Override
	public JsonNode getProcessed() {
		ArrayNode node = JsonNodeFactory.instance.arrayNode();
		for (LightblueResponse resp : resps) {
			node.add(resp.getProcessed());
		}
		return node;
	}

	@Override
	public <T> T parseProcessed(Class<T> type) throws LightblueResponseParseException {
		throw new UnsupportedOperationException();
	}

	public <T extends AbstractLightblueRequest> LightblueResponse getResponse(LightblueRequest lbr) {
		return resps.get(reqs.indexOf(lbr));
	}

}

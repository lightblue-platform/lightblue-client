/**
 * 
 */
package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

	private List<LightblueResponse> responses = new ArrayList<LightblueResponse>();
	private List<AbstractBulkLightblueRequest> requests = new ArrayList<AbstractBulkLightblueRequest>();

	private JsonNode json;
	private String text;

	public BulkLightblueResponse() {
	}

	public BulkLightblueResponse(List<LightblueResponse> resps, List<AbstractBulkLightblueRequest> reqs) {
		this.responses = resps;
		this.requests = reqs;
	}

	public BulkLightblueResponse(String responseText, List<AbstractBulkLightblueRequest> reqs) throws LightblueResponseParseException, LightblueException {
		text = responseText;
		try {
			json = JSON.getDefaultObjectMapper().readTree(responseText);
			ArrayNode resps = (ArrayNode) json.get("responses");
			for (Iterator<JsonNode> it = resps.iterator(); it.hasNext();) {
				JsonNode resp = it.next();
				LightblueResponse response = new DefaultLightblueResponse(resp.get("response").toString());
				JsonNode seq = resp.get("seq");
				if (!seq.isNumber()) {
					throw new LightblueException("Invalid sequence.", response);
				}
				responses.add(resp.get("seq").intValue(), response);
			}
		} catch (IOException e) {
			throw new LightblueResponseParseException("Error parsing lightblue response: " + responseText + "\n", e);
		}

	}

	@Override
	public String getText() {
		if (text == null || text.isEmpty()) {
			text = getJson().toString();
		}
		return text;
	}

	@Override
	public JsonNode getJson() {
		if (json == null || json.toString().isEmpty()) {
			ObjectNode root = JsonNodeFactory.instance.objectNode();
			ArrayNode resps = JsonNodeFactory.instance.arrayNode();
			for (LightblueResponse resp : responses) {
				ObjectNode seqNode = JsonNodeFactory.instance.objectNode();
				seqNode.set("seq", JSON.toJsonNode(responses.indexOf(resp)));
				seqNode.set("response", resp.getJson());
				resps.add(seqNode);
			}
			root.set("responsess", resps);
			json = root;
		}
		return json;
	}

	@Override
	public boolean hasError() {
		for (LightblueResponse resp : responses) {
			if (resp.hasError()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasDataErrors() {
		for (LightblueResponse resp : responses) {
			if (resp.hasDataErrors()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Error[] getErrors() {
		List<Error> errors = new ArrayList<Error>();
		for (LightblueResponse resp : responses) {
			errors.addAll(Arrays.asList(resp.getErrors()));
		}
		return (Error[]) errors.toArray();
	}

	@Override
	public DataError[] getDataErrors() {
		List<DataError> errors = new ArrayList<DataError>();
		for (LightblueResponse resp : responses) {
			errors.addAll(Arrays.asList(resp.getDataErrors()));
		}
		return (DataError[]) errors.toArray();
	}

	@Override
	public int parseModifiedCount() {
		int mod = 0;
		for (LightblueResponse resp : responses) {
			mod += resp.parseModifiedCount();
		}
		return mod;
	}

	@Override
	public int parseMatchCount() {
		int match = 0;
		for (LightblueResponse resp : responses) {
			match += resp.parseMatchCount();
		}
		return match;
	}

	@Override
	public JsonNode getProcessed() {
		ArrayNode node = JsonNodeFactory.instance.arrayNode();
		for (LightblueResponse resp : responses) {
			node.add(resp.getProcessed());
		}
		return node;
	}

	@Override
	public <T> T parseProcessed(Class<T> type) throws LightblueResponseParseException {
		throw new UnsupportedOperationException();
	}

	public <T extends AbstractLightblueRequest> LightblueResponse getResponse(LightblueRequest lbr) {
		return responses.get(requests.indexOf(lbr));
	}

	public List<LightblueResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<LightblueResponse> responses) {
		this.responses = responses;
	}

	public List<AbstractBulkLightblueRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<AbstractBulkLightblueRequest> requests) {
		this.requests = requests;
	}

}

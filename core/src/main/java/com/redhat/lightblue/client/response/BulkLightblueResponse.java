/**
 * 
 */
package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class BulkLightblueResponse {

	private final List<LightblueResponse> responses;
	private final List<? super AbstractLightblueRequest> requests;

	private JsonNode json;
	private String text;

	public BulkLightblueResponse(List<LightblueResponse> resps, AbstractBulkLightblueRequest<? extends AbstractLightblueRequest> reqs) {
		responses = new ArrayList<LightblueResponse>(resps);
		requests = new ArrayList<AbstractLightblueRequest>(reqs.getRequests());
	}

	public BulkLightblueResponse(String responseText, AbstractBulkLightblueRequest<? extends AbstractLightblueRequest> reqs) throws LightblueResponseParseException,
			LightblueException {
		this(Collections.<LightblueResponse> emptyList(), reqs);
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

	public String getText() {
		if (text == null || text.isEmpty()) {
			text = getJson().toString();
		}
		return text;
	}

	public JsonNode getJson() {
		if (json == null || json.toString().isEmpty()) {
			ObjectNode root = JsonNodeFactory.instance.objectNode();
			ArrayNode resps = JsonNodeFactory.instance.arrayNode();
			for (LightblueResponse resp : responses) {
				ObjectNode seqNode = JsonNodeFactory.instance.objectNode();
				seqNode.set("seq", JsonNodeFactory.instance.numberNode(responses.indexOf(resp)));
				seqNode.set("response", resp.getJson());
				resps.add(seqNode);
			}
			root.set("responsess", resps);
			json = root;
		}
		return json;
	}

	public LightblueResponse getResponse(LightblueRequest lbr) {
		return responses.get(requests.indexOf(lbr));
	}

	public List<LightblueResponse> getResponses() {
		return responses;
	}

	public List<? super AbstractLightblueRequest> getRequests() {
		return requests;
	}
}

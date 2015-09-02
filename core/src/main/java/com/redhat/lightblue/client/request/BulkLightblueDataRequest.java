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
import com.redhat.lightblue.client.util.JSON;

/**
 * @author bvulaj
 *
 */
public class BulkLightblueDataRequest implements LightblueRequest {

	private List<Sequence> seqs = new ArrayList<Sequence>();

	public BulkLightblueDataRequest(List<AbstractLightblueDataRequest> alrs) {
		for (AbstractLightblueDataRequest alr : alrs) {
			this.seqs.add(new Sequence(seqs.size(), alr));

		}
	}

	@Override
	public JsonNode getBodyJson() {
		ArrayNode node = JsonNodeFactory.instance.arrayNode();
		for (Sequence seq : seqs) {
			ObjectNode seqNode = JsonNodeFactory.instance.objectNode();
			seqNode.set("seq", JSON.toJsonNode(seq.seq));
			seqNode.set("op", JSON.toJsonNode(seq.request.getOperationPathParam()));
			seqNode.set("request", seq.request.getBodyJson());
			node.add(seqNode);
		}
		return node;
	}

	@Override
	public String getBody() {
		// TODO Auto-generated method stub
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

	private static class Sequence {
		private final int seq;
		private final AbstractLightblueDataRequest request;

		public Sequence(int seq, AbstractLightblueDataRequest request) {
			this.seq = seq;
			this.request = request;
		}
	}

}

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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;
import com.redhat.lightblue.client.request.AbstractDataBulkRequest;
import com.redhat.lightblue.client.request.AbstractLightblueRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.util.JSON;

/**
 * @author bvulaj
 *
 */
public class BulkLightblueResponse {

    private final Map<Integer, LightblueResponse> responses = new TreeMap<Integer, LightblueResponse>();
    private final List<? extends AbstractLightblueRequest> requests;
    private JsonNode json;
    private String text;

    public BulkLightblueResponse(String responseText, AbstractDataBulkRequest<? extends AbstractLightblueRequest> reqs) throws LightblueResponseParseException,
            LightblueException {
        requests = reqs.getRequests();
        this.text = responseText;
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
                responses.put(resp.get("seq").intValue(), response);
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
        if (json == null) {
            ObjectNode root = JsonNodeFactory.instance.objectNode();
            ArrayNode resps = JsonNodeFactory.instance.arrayNode();
            for (Entry<Integer, LightblueResponse> resp : responses.entrySet()) {
                ObjectNode seqNode = JsonNodeFactory.instance.objectNode();
                seqNode.set("seq", JsonNodeFactory.instance.numberNode(resp.getKey()));
                seqNode.set("response", resp.getValue().getJson());
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
        return new ArrayList<LightblueResponse>(responses.values());
    }

    public List<? extends AbstractLightblueRequest> getRequests() {
        return requests;
    }
}

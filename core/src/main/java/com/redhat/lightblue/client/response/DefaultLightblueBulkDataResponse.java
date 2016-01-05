package com.redhat.lightblue.client.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.request.AbstractDataBulkRequest;
import com.redhat.lightblue.client.request.AbstractLightblueRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.util.JSON;

/**
 * @author bvulaj
 *
 */
public class DefaultLightblueBulkDataResponse extends AbstractLightblueResponse implements LightblueBulkDataResponse {

    private final Map<Integer, LightblueDataResponse> responses = new TreeMap<>();
    private final List<? extends AbstractLightblueRequest> requests;

    public DefaultLightblueBulkDataResponse(String responseText, AbstractDataBulkRequest<? extends AbstractLightblueRequest> reqs) throws LightblueParseException, LightblueResponseException {
        this(responseText, JSON.getDefaultObjectMapper(), reqs);
    }

    public DefaultLightblueBulkDataResponse(String responseText, ObjectMapper mapper, AbstractDataBulkRequest<? extends AbstractLightblueRequest> reqs) throws LightblueParseException, LightblueResponseException {
        super(responseText, mapper);
        requests = reqs.getRequests();

        JsonNode resps = getJson().get("responses");

        if (resps.isArray()) {
            ArrayNode arrResps = (ArrayNode) resps;
            for (Iterator<JsonNode> it = arrResps.iterator(); it.hasNext();) {
                JsonNode resp = it.next();
                DefaultLightblueDataResponse response = new DefaultLightblueDataResponse(resp.get("response").toString());
                JsonNode seq = resp.get("seq");
                if (!seq.isNumber()) {
                    throw new LightblueResponseException("Invalid sequence.", response);
                }
                responses.put(resp.get("seq").intValue(), response);
            }
        }
    }

    @Override
    public LightblueDataResponse getResponse(LightblueRequest lbr) {
        return getResponse(requests.indexOf(lbr));
    }

    @Override
    public LightblueDataResponse getResponse(int seq) {
        return responses.get(seq);
    }

    @Override
    public List<LightblueDataResponse> getResponses() {
        return new ArrayList<>(responses.values());
    }

    @Override
    public List<? extends AbstractLightblueRequest> getRequests() {
        return requests;
    }
}

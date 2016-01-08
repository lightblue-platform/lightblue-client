package com.redhat.lightblue.client.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public DefaultLightblueBulkDataResponse(String responseText, AbstractDataBulkRequest<? extends AbstractLightblueRequest> reqs) throws LightblueParseException, LightblueBulkResponseException {
        this(responseText, JSON.getDefaultObjectMapper(), reqs);
    }

    public DefaultLightblueBulkDataResponse(String responseText, ObjectMapper mapper, AbstractDataBulkRequest<? extends AbstractLightblueRequest> reqs) throws LightblueParseException, LightblueBulkResponseException {
        super(responseText, mapper);
        requests = reqs.getRequests();

        JsonNode resps = getJson().get("responses");
        if (resps == null) {
            throw new LightblueParseException("Unable to parse 'responses' node.");
        }

        Map<Integer, LightblueResponseException> erroredResponses = new HashMap<>();

        if (resps.isArray()) {
            ArrayNode arrResps = (ArrayNode) resps;
            for (Iterator<JsonNode> it = arrResps.iterator(); it.hasNext();) {
                JsonNode resp = it.next();
                JsonNode seq = resp.get("seq");

                if (!seq.isNumber()) {
                    // A invalid sequence should not be possible, so it indicates a bad bulk response.
                    throw new LightblueParseException("Invalid sequence: " + seq.toString());
                }

                int seqNumber = seq.intValue();
                try {
                    responses.put(seqNumber, new DefaultLightblueDataResponse(resp.get("response"), mapper));
                } catch (LightblueResponseException e) {
                    erroredResponses.put(seqNumber, e);

                    //Append the response because it is still a valid errored response and seq needs to be kept.
                    responses.put(seqNumber, (DefaultLightblueDataResponse) e.getLightblueResponse());
                }
            }
        }
        else {
            throw new LightblueParseException("Unparseable bulk data 'responses' node");
        }

        if (!erroredResponses.isEmpty()) {
            throw new LightblueBulkResponseException("Errors returned in responses", this, erroredResponses);
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
        return Collections.unmodifiableList(new ArrayList<>(responses.values()));
    }

    @Override
    public List<? extends AbstractLightblueRequest> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    @Override
    public Set<Integer> getSeqNumbers() {
        return Collections.unmodifiableSet(responses.keySet());
    }

}

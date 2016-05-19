package com.redhat.lightblue.client.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.request.AbstractDataBulkRequest;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.util.JSON;

/**
 * @author bvulaj
 *
 */
public class DefaultLightblueBulkDataResponse extends AbstractLightblueResponse implements LightblueBulkDataResponse, LightblueBulkDataErrorResponse {

    private final SortedMap<Integer, LightblueDataResponse> responsesSuccessful = new TreeMap<>();
    private SortedMap<Integer, LightblueDataResponse> responsesErrored;
    private final List<? extends AbstractLightblueDataRequest> requests;

    public DefaultLightblueBulkDataResponse(String responseText, AbstractDataBulkRequest<? extends AbstractLightblueDataRequest> reqs) throws LightblueParseException, LightblueBulkResponseException, LightblueException {
        this(responseText, JSON.getDefaultObjectMapper(), reqs);
    }

    public DefaultLightblueBulkDataResponse(String responseText, ObjectMapper mapper, AbstractDataBulkRequest<? extends AbstractLightblueDataRequest> reqs) throws LightblueParseException, LightblueBulkResponseException, LightblueException {
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
                    responsesSuccessful.put(seqNumber, new DefaultLightblueDataResponse(resp.get("response"), mapper));
                } catch (LightblueResponseException e) {
                    erroredResponses.put(seqNumber, e);
                    if (responsesErrored == null) {
                        responsesErrored = new TreeMap<>();
                    }
                    responsesErrored.put(seqNumber, (DefaultLightblueDataResponse) e.getLightblueResponse());
                }
            }
        } else {
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
        return getSequencedResponses().get(seq);
    }

    @Override
    public List<LightblueDataResponse> getResponses() {
        return Collections.unmodifiableList(new ArrayList<>(getSequencedResponses().values()));
    }

    @Override
    public List<? extends AbstractLightblueDataRequest> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    @Override
    public SortedMap<Integer, LightblueDataResponse> getSequencedResponses() {
        SortedMap<Integer, LightblueDataResponse> allResponses = getSequencedSuccessfulResponses();
        if (responsesErrored != null) {
            allResponses.putAll(responsesErrored);
        }
        return allResponses;
    }

    @Override
    public SortedMap<Integer, LightblueDataResponse> getSequencedSuccessfulResponses() {
        return new TreeMap<>(responsesSuccessful);
    }

    @Override
    public SortedMap<Integer, LightblueDataResponse> getSequencedResponsesWithErrors() {
        return new TreeMap<>(responsesErrored);
    }

    @Override
    public List<LightblueDataResponse> getSuccessfulResponses() {
        return Collections.unmodifiableList(new ArrayList<>(responsesSuccessful.values()));
    }

    @Override
    public List<LightblueDataResponse> getResponsesWithErrors() {
        return Collections.unmodifiableList(new ArrayList<>(responsesErrored.values()));
    }

}

package com.redhat.lightblue.client.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class LightblueBulkResponseException extends LightblueException {

    private static final long serialVersionUID = -1204842853642316889L;

    private final LightblueBulkDataResponse bulkResponse;
    private final Map<Integer, LightblueResponseException> erroredResponses;

    public LightblueBulkResponseException(String message,
            LightblueBulkDataResponse bulkResponse,
            Map<Integer, LightblueResponseException> erroredResponses) {
        super(message);
        this.bulkResponse = bulkResponse;
        this.erroredResponses = erroredResponses;
    }

    public LightblueBulkDataResponse getBulkResponse() {
        return bulkResponse;
    }

    public Map<Integer, LightblueResponseException> getLightblueResponseExceptions() {
        return Collections.unmodifiableMap(erroredResponses);
    }

    public SortedMap<Integer, LightblueDataResponse> getSuccessfulResponsesWithSeq() {
        Set<Integer> successfulKeys = new HashSet<>(bulkResponse.getSeqNumbers());
        successfulKeys.removeAll(erroredResponses.keySet());

        SortedMap<Integer, LightblueDataResponse> responses = new TreeMap<>();
        for (Integer seq : successfulKeys) {
            responses.put(seq, bulkResponse.getResponse(seq));
        }
        return responses;
    }

    public List<LightblueDataResponse> getSuccessfulResponses() {
        return Collections.unmodifiableList(new ArrayList<>(getSuccessfulResponsesWithSeq().values()));
    }

}

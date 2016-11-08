package com.redhat.lightblue.client.response;

import java.util.List;
import java.util.SortedMap;

import com.redhat.lightblue.client.request.LightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.CRUDRequest;

public interface LightblueBulkDataResponse extends LightblueResponse {

    /**
     * @param lbr - {@link LightblueRequest}
     * @return the corresponding {@link LightblueDataResponse} for the passed in
     * {@link LightblueRequest}.
     */
    LightblueDataResponse getResponse(LightblueRequest lbr);

    /**
     * @param seq - sequence number
     * @return the corresponding {@link LightblueDataResponse} for the passed in
     * <code>seq</code>.
     */
    LightblueDataResponse getResponse(int seq);

    /**
     * @return all the {@link LightblueDataResponse}s in a sequentially ordered
     * {@link List}.
     */
    List<LightblueDataResponse> getResponses();

    /**
     * @return the {@link AbstractLightblueDataRequest} that make up this
     * {@link LightblueBulkDataResponse}.
     */
    List<? extends CRUDRequest> getRequests();

    /**
     * @return A {@link SortedMap} of {@link LightblueDataResponse}s keyed off
     * the sequence.
     */
    SortedMap<Integer, LightblueDataResponse> getSequencedResponses();

}

package com.redhat.lightblue.client.response;

import java.util.List;
import java.util.SortedMap;

public interface LightblueBulkDataErrorResponse extends LightblueBulkDataResponse {

    /**
     * @return A {@link SortedMap} of {@link LightblueDataResponse}s keyed off
     * the sequence. Includes both successful and errored responses.
     */
    @Override
    SortedMap<Integer, LightblueDataResponse> getSequencedResponses();

    /**
     * @return all the {@link LightblueDataResponse}s in a sequentially ordered
     * {@link List}. Includes both successful and errored responses.
     */
    @Override
    List<LightblueDataResponse> getResponses();

    /**
     * @return A {@link SortedMap} of successful {@link LightblueDataResponse}s
     * keyed off the sequence.
     */
    SortedMap<Integer, LightblueDataResponse> getSequencedSuccessfulResponses();

    /**
     * @return all of the successful {@link LightblueDataResponse}s in a
     * sequentially ordered {@link List}.
     */
    List<LightblueDataResponse> getSuccessfulResponses();

    /**
     * @return A {@link SortedMap} of errored {@link LightblueDataResponse}s
     * keyed off the sequence.
     */
    SortedMap<Integer, LightblueDataResponse> getSequencedResponsesWithErrors();

    /**
     * @return all of the errored {@link LightblueDataResponse}s in a
     * sequentially ordered {@link List}.
     */
    List<LightblueDataResponse> getResponsesWithErrors();

}

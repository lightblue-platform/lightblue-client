package com.redhat.lightblue.client.response;

import java.util.Collections;
import java.util.Map;

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

}

package com.redhat.lightblue.client.response;

import java.util.Collections;
import java.util.Map;

import com.redhat.lightblue.client.LightblueException;

public class LightblueBulkResponseException extends LightblueException {

    private static final long serialVersionUID = -1204842853642316889L;

    private final LightblueBulkDataErrorResponse bulkResponse;
    private final Map<Integer, LightblueResponseException> erroredResponseExceptions;

    public LightblueBulkResponseException(String message,
            LightblueBulkDataErrorResponse bulkResponse,
            Map<Integer, LightblueResponseException> erroredResponseExceptions) {
        super(message);
        this.bulkResponse = bulkResponse;
        this.erroredResponseExceptions = erroredResponseExceptions;
    }

    public LightblueBulkDataErrorResponse getBulkResponse() {
        return bulkResponse;
    }

    public Map<Integer, LightblueResponseException> getLightblueResponseExceptions() {
        return Collections.unmodifiableMap(erroredResponseExceptions);
    }

}

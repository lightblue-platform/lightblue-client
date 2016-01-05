package com.redhat.lightblue.client.response;

import java.util.Collections;
import java.util.List;

public class BulkResponseException extends LightblueException {

    private static final long serialVersionUID = -1204842853642316889L;

    private final LightblueBulkDataResponse bulkResponse;
    private final List<? extends LightblueResponseException> exceptions;

    public BulkResponseException(String message,
            LightblueBulkDataResponse bulkResponse,
            List<? extends LightblueResponseException> exceptions) {
        super(message);
        this.bulkResponse = bulkResponse;
        this.exceptions = exceptions;
    }

    public LightblueBulkDataResponse getBulkResponse() {
        return bulkResponse;
    }

    public List<? extends LightblueResponseException> getLightblueResponseExceptions() {
        return Collections.unmodifiableList(exceptions);
    }

}

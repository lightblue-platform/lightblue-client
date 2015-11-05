package com.redhat.lightblue.client.http;

import com.redhat.lightblue.client.response.LightblueException;

/**
 * When lightblue does not return a response at all.
 *
 * @author dcrissman
 */
public class LightblueHttpClientException extends LightblueException {

    private static final long serialVersionUID = 2052652670507326767L;

    private final String httpResponseBody;
    private final Integer httpStatus;

    public LightblueHttpClientException(Throwable cause) {
        super(cause);
        httpResponseBody = null;
        httpStatus = null;
    }

    public LightblueHttpClientException(Throwable cause, int httpStatus, String responseBody) {
        super("Lightblue http response status: "+httpStatus, cause);
        this.httpResponseBody=responseBody;
        this.httpStatus=httpStatus;
    }

    public String getHttpResponseBody() {
        return httpResponseBody;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

}

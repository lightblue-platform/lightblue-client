package com.redhat.lightblue.client.http;

/**
 * Exception indicating an error ocurred while communicating with lightblue.
 *
 * @author dcrissman
 */
public class LightblueHttpClientException extends RuntimeException {

    private static final long serialVersionUID = 2052652670507326767L;

    public LightblueHttpClientException(String message) {
        super(message);
    }

    public LightblueHttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

}

package com.redhat.lightblue.client.response;

/**
 * Exception thrown when an error status is returned from Lightblue.
 *
 * @author dcrissman
 */
public class LightblueErrorResponseException extends RuntimeException {

    private static final long serialVersionUID = -3732433923527169586L;

    public LightblueErrorResponseException(String message) {
        super(message);
    }

    public LightblueErrorResponseException(Throwable cause) {
        super(cause);
    }

    public LightblueErrorResponseException(String message, Throwable cause) {
        super(message, cause);
    }

}

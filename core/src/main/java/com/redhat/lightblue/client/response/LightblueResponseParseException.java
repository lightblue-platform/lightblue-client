package com.redhat.lightblue.client.response;

/**
 * Exception thrown when a response from lightblue is not able to be parsed.
 *
 * @author dcrissman
 */
public class LightblueResponseParseException extends Exception {

    private static final long serialVersionUID = -1221306072042538444L;

    public LightblueResponseParseException(String message) {
        super(message);
    }

    public LightblueResponseParseException(Throwable cause) {
        super(cause);
    }

    public LightblueResponseParseException(String message, Throwable cause) {
        super(message, cause);
    }

}

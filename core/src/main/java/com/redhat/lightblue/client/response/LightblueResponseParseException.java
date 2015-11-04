package com.redhat.lightblue.client.response;

/**
 * Exception thrown when json2pojo conversion fails.
 *
 * @author dcrissman
 */
public class LightblueResponseParseException extends LightblueException {

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

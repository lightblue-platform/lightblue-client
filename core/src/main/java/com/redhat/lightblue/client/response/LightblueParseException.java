package com.redhat.lightblue.client.response;

import com.redhat.lightblue.client.LightblueException;

/**
 * Exception thrown when json2pojo conversion fails.
 *
 * @author dcrissman
 */
public class LightblueParseException extends LightblueException {

    private static final long serialVersionUID = -1221306072042538444L;

    public LightblueParseException(String message) {
        super(message);
    }

    public LightblueParseException(Throwable cause) {
        super(cause);
    }

    public LightblueParseException(String message, Throwable cause) {
        super(message, cause);
    }

}

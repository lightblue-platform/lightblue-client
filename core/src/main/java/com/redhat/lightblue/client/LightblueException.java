package com.redhat.lightblue.client;

/**
 * Parent of all lightblue exceptions.
 *
 * @author mpatercz
 *
 */
public class LightblueException extends Exception {

    private static final long serialVersionUID = -5883690817679713469L;

    public LightblueException() {
        super();
    }

    public LightblueException(String message) {
        super(message);
    }

    public LightblueException(Throwable cause) {
        super(cause);
    }

    public LightblueException(String message, Throwable cause) {
        super(message, cause);
    }

}

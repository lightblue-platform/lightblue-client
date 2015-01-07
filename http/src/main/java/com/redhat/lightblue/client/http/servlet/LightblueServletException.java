package com.redhat.lightblue.client.http.servlet;

import javax.servlet.ServletException;

public class LightblueServletException extends ServletException {
    public LightblueServletException(String message) {
        super(message);
    }

    public LightblueServletException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}

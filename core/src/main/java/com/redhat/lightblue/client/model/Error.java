package com.redhat.lightblue.client.model;

import java.util.ArrayDeque;
import java.util.StringTokenizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Error object. Maintains an error code, message, and context of the error. The
 * context works as a stack of context information that can be passed to the
 * client as an indicator of where the error happened.
 * 
 */
public final class Error extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final char DELIMITER = '/';

    private final ArrayDeque<String> context;
    private final String errorCode;
    private final String msg;
    
    public Error(String errorCode, String msg) {
        this.context = new ArrayDeque<>();
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public ArrayDeque<String> getContext() {
        return context;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void pushContext(String context) {
        this.context.addLast(context);
    }

    public static Error fromJson(JsonNode node) {
        if (node instanceof ObjectNode) {
            String e = null;
            String m = null;

            JsonNode x;

            x = ((ObjectNode) node).get("errorCode");
            if (x != null) {
                e = x.asText();
            }
            x = ((ObjectNode) node).get("msg");
            if (x != null) {
                m = x.asText();
            }

            Error ret = new Error(e, m);

            x = ((ObjectNode) node).get("context");
            if (x != null) {
                StringTokenizer tok = new StringTokenizer(x.asText(), "/");
                while (tok.hasMoreTokens()) {
                    ret.pushContext(tok.nextToken());
                }
            }
            return ret;
        }
        return null;
    }
}

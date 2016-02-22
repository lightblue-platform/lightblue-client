package com.redhat.lightblue.client.model;

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

    private final String context;
    private final String errorCode;
    private final String msg;

    public Error(String context, String errorCode, String msg) {
        this.context = context;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public String getContext() {
        return context;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public static Error fromJson(JsonNode node) {
        if (node instanceof ObjectNode) {
            String e = null;
            String m = null;
            String c = null;

            JsonNode x;
            x = ((ObjectNode) node).get("context");
            if (x != null) {
                c = x.asText();
            }
            x = ((ObjectNode) node).get("errorCode");
            if (x != null) {
                e = x.asText();
            }
            x = ((ObjectNode) node).get("msg");
            if (x != null) {
                m = x.asText();
            }
            return new Error(c, e, m);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Error [context=" + context + ", errorCode=" + errorCode + ", msg=" + msg + "]";
    }
}

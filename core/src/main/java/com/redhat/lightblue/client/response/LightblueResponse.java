package com.redhat.lightblue.client.response;

import com.fasterxml.jackson.databind.JsonNode;

public interface LightblueResponse {
    String getText();

    JsonNode getJson();

    boolean hasError();

    boolean hasDataErrors();

    JsonNode[] getErrors();
    JsonNode[] getDataErrors();

    int parseModifiedCount();

    int parseMatchCount();

    <T> T parseProcessed(Class<T> type) throws LightblueResponseParseException;
}

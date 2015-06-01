package com.redhat.lightblue.client.response;

import com.fasterxml.jackson.databind.JsonNode;

public interface LightblueResponse {
    String getText();

    JsonNode getJson();

    boolean hasError();

    int parseModifiedCount();

    int parseMatchCount();

    <T> T parseProcessed(Class<T> type) throws LightblueResponseParseException;
}

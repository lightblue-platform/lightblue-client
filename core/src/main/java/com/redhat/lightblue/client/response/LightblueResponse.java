package com.redhat.lightblue.client.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;

public interface LightblueResponse {
    String getText();

    JsonNode getJson();

    boolean hasError();

    boolean hasDataErrors();

    Error[] getErrors();
    DataError[] getDataErrors();

    int parseModifiedCount();

    int parseMatchCount();

    JsonNode parseProcessed();

    <T> T parseProcessed(Class<T> type) throws LightblueResponseParseException;
}

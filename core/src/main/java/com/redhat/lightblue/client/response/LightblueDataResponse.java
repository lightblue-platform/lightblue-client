package com.redhat.lightblue.client.response;

import com.fasterxml.jackson.databind.JsonNode;

public interface LightblueDataResponse extends LightblueResponse {

    int parseModifiedCount();

    int parseMatchCount();

    JsonNode getProcessed();

    ResultMetadata[] getResultMetadata() throws LightblueParseException;

    <T> T parseProcessed(Class<T> type) throws LightblueParseException;

}

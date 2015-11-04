package com.redhat.lightblue.client.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Successful lightblue response. No errors.
 *
 *
 */
public interface LightblueResponse {
    String getText();

    JsonNode getJson();

    int parseModifiedCount();

    int parseMatchCount();

    JsonNode getProcessed();

    <T> T parseProcessed(Class<T> type) throws LightblueParseException;
}

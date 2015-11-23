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

}

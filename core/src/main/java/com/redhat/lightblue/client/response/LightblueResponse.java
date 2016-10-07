package com.redhat.lightblue.client.response;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Successful lightblue response. No errors.
 *
 *
 */
public interface LightblueResponse {

    String getText();

    JsonNode getJson();

    List<String> getHeaderValues(String key);

}

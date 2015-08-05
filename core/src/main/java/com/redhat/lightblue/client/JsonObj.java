package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * An object that can be converted to json
 */
public interface JsonObj {
    JsonNode toJson();
}

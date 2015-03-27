package com.redhat.lightblue.client.json;

import java.util.Map;

/**
 * Helper for serializing/deserializing json strings
 *
 * @author jjunquei
 *
 */
public interface LightBlueJsonHelper {

    /**
     * Returns a Map representation of a JSON string
     *
     * @param json
     * @return A map representation of a JSON string or null if the json could
     * not be parsed
     */
    public Map<String, Object> getJsonMap(String json);

    public String createEntityPutRequestJson(String entity, String version, Map<String, Object> data);

}

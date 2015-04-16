package com.redhat.lightblue.client.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.redhat.lightblue.client.util.JSON;

public class LightBlueJsonHelperImpl implements LightBlueJsonHelper {

    @Override
    public Map<String, Object> getJsonMap(String json) {
        HashMap<String, Object> result = null;
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};

        try {
            result = JSON.getDefaultObjectMapper().readValue(json, typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String createEntityPutRequestJson(String entity, String version,
            Map<String, Object> data) {

        return null;
    }
}

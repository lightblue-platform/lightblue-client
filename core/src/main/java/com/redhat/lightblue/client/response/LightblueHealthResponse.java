package com.redhat.lightblue.client.response;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueException;

/**
 * @author ssaurabh
 *
 */
/**
 * @author ssaurabh
 *
 */
public class LightblueHealthResponse extends AbstractLightblueResponse implements LightblueResponse {

    private static final long serialVersionUID = 5383951657430651771L;

    private boolean isHealthy;

    public LightblueHealthResponse(JsonNode responseNode, Map<String, List<String>> headers)
            throws LightblueResponseException, LightblueException {
        super(responseNode, headers);
    }

    public LightblueHealthResponse(JsonNode responseNode, ObjectMapper mapper)
            throws LightblueResponseException, LightblueException {
        super(responseNode, null, mapper);
    }

    public LightblueHealthResponse(JsonNode responseNode, Map<String, List<String>> headers, ObjectMapper mapper)
            throws LightblueResponseException, LightblueException {
        super(responseNode, headers, mapper);
    }

    public LightblueHealthResponse(String responseText)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, null);
    }

    public LightblueHealthResponse(String responseText, Map<String, List<String>> headers)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, headers);
    }

    public LightblueHealthResponse(String responseText, Map<String, List<String>> headers, ObjectMapper mapper)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, headers, mapper);
    }

    public boolean isHealthy() {
        return parseHealthy();
    }

    
    /**
     * parses the lightblue health response and returns true only if healthy is
     * true for all the nodes in the response, otherwise returns false
     */
    public boolean parseHealthy() {

        Iterator<JsonNode> iterator = getJson().elements();

        while (iterator.hasNext()) {
            JsonNode jsonNode = (JsonNode) iterator.next();

            if (!jsonNode.get("healthy").asBoolean()) {
                return false;
            }
        }

        return true;
    }
}

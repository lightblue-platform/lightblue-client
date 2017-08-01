package com.redhat.lightblue.client.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueException;

public class DefaultLightblueDiagnosticsResponse extends AbstractLightblueResponse
        implements LightblueDiagnosticsResponse {

    private static final long serialVersionUID = 5383951657430651771L;

    public DefaultLightblueDiagnosticsResponse(JsonNode responseNode, Map<String, List<String>> headers)
            throws LightblueResponseException, LightblueException {
        super(responseNode, headers);
    }

    public DefaultLightblueDiagnosticsResponse(JsonNode responseNode, ObjectMapper mapper)
            throws LightblueResponseException, LightblueException {
        super(responseNode, null, mapper);
    }

    public DefaultLightblueDiagnosticsResponse(JsonNode responseNode, Map<String, List<String>> headers,
            ObjectMapper mapper) throws LightblueResponseException, LightblueException {
        super(responseNode, headers, mapper);
    }

    public DefaultLightblueDiagnosticsResponse(String responseText)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, null);
    }

    public DefaultLightblueDiagnosticsResponse(String responseText, Map<String, List<String>> headers)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, headers);
    }

    public DefaultLightblueDiagnosticsResponse(String responseText, Map<String, List<String>> headers,
            ObjectMapper mapper) throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, headers, mapper);
    }

    @Override
    public DiagnosticsElement getDiagnostics(String diagnosticsElementName) {
        JsonNode node = getJson().get(diagnosticsElementName);

        boolean isHealthy = false;
        String message = null;

        if (node != null) {
            if (node.get("healthy") != null) {
                isHealthy = node.get("healthy").asBoolean();
            }

            if (node.get("message") != null) {
                message = node.get("message").asText();
            }

            return new DiagnosticsElement(diagnosticsElementName, isHealthy, message);
        }

        return null;
    }
}

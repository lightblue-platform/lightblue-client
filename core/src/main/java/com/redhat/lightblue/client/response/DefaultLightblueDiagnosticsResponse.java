package com.redhat.lightblue.client.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.util.JSON;

public class DefaultLightblueDiagnosticsResponse extends AbstractLightblueResponse
        implements LightblueDiagnosticsResponse {

    private static final String MESSAGE = "message";
    private static final String HEALTHY = "healthy";
    private static final long serialVersionUID = 5383951657430651771L;

    public DefaultLightblueDiagnosticsResponse(String responseText, Map<String, List<String>> headers)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        this(responseText, headers, JSON.getDefaultObjectMapper());
    }

    public DefaultLightblueDiagnosticsResponse(String responseText, Map<String, List<String>> headers,
            ObjectMapper mapper) throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, headers, mapper);
    }

    @Override
    public DiagnosticsElement getDiagnostics(String diagnosticsElementName) {

        if (!hasDiagnostics(diagnosticsElementName)) {
            throw new NoSuchElementException(diagnosticsElementName);
        }

        JsonNode node = getJson().get(diagnosticsElementName);

        boolean isHealthy = false;
        String message = null;

        if (node.get(HEALTHY) != null) {
            isHealthy = node.get(HEALTHY).asBoolean();
        }

        if (node.get(MESSAGE) != null) {
            message = node.get(MESSAGE).asText();
        }

        return new DiagnosticsElement(diagnosticsElementName, isHealthy, message);

    }

    @Override
    public boolean hasDiagnostics(String diagnosticsElementName) {
        return (getJson().get(diagnosticsElementName) != null);
    }

    @Override
    public List<DiagnosticsElement> getDiagnostics() {
        List<DiagnosticsElement> diagnosticsElements = new ArrayList<>();

        Iterator<String> fieldNames = getJson().fieldNames();
        boolean isHealthy = false;
        String message = null;

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode jsonNode = getJson().get(fieldName);

            if (jsonNode != null) {
                if (jsonNode.get(HEALTHY) != null) {
                    isHealthy = jsonNode.get(HEALTHY).asBoolean();
                }

                if (jsonNode.get(MESSAGE) != null) {
                    message = jsonNode.get(MESSAGE).asText();
                }

                diagnosticsElements.add(new DiagnosticsElement(fieldName, isHealthy, message));
            }
        }

        return diagnosticsElements;
    }
}

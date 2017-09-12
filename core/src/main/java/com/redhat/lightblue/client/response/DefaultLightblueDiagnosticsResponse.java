package com.redhat.lightblue.client.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.util.JSON;

public class DefaultLightblueDiagnosticsResponse extends AbstractLightblueResponse
        implements LightblueDiagnosticsResponse {

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
        
        Map<String, String> parameters = new LinkedHashMap<>();
        Iterator<Entry<String, JsonNode>> iterator = node.fields();
        
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) iterator.next();
            parameters.put(entry.getKey(), entry.getValue().asText());
        }

        return new DiagnosticsElement(diagnosticsElementName, parameters);

    }

    @Override
    public boolean hasDiagnostics(String diagnosticsElementName) {
        return (getJson().get(diagnosticsElementName) != null);
    }

    @Override
    public List<DiagnosticsElement> getDiagnostics() {
        List<DiagnosticsElement> diagnosticsElements = new ArrayList<>();

        if (getJson() != null) {
            Iterator<String> fieldNames = getJson().fieldNames();
            
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode jsonNode = getJson().get(fieldName);
                
                if (jsonNode != null) {
                    Map<String, String> parameters = new LinkedHashMap<>();
                    Iterator<Entry<String, JsonNode>> iterator = jsonNode.fields();
                    
                    while (iterator.hasNext()) {
                        Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) iterator.next();
                        parameters.put(entry.getKey(), entry.getValue().asText());
                    }

                    diagnosticsElements.add(new DiagnosticsElement(fieldName, parameters));
                }
            }
        }

        return diagnosticsElements;
    }

    @Override
    public boolean allHealthy() {
        
        List<DiagnosticsElement> diagnosticsElements = getDiagnostics();
        
        for (DiagnosticsElement element : diagnosticsElements){
            if(!element.isHealthy()){
                return false;
            }
        }
        return true;
    }
}
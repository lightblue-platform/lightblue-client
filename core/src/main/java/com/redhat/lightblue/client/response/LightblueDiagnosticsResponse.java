package com.redhat.lightblue.client.response;

import java.util.List;

public interface LightblueDiagnosticsResponse extends LightblueResponse {

    DiagnosticsElement getDiagnostics(String diagnosticsElementName);
    
    boolean hasDiagnostics(String diagnosticsElementName);

    List<DiagnosticsElement> getDiagnostics();
}

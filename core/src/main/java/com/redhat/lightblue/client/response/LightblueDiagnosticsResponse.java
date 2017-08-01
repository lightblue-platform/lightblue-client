package com.redhat.lightblue.client.response;

public interface LightblueDiagnosticsResponse extends LightblueResponse {

    DiagnosticsElement getDiagnostics(String diagnosticsElementName);
}

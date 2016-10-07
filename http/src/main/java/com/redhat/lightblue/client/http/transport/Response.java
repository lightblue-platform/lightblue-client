package com.redhat.lightblue.client.http.transport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {

    private final Map<String, List<String>> headers;
    private final String body;

    protected Response(final String body, final Map<String, List<String>> headers) {
        this.body = body;
        this.headers = headers;
    }

    public Map<String, List<String>> getHeaders() {
        if (headers == null) {
            return null;
        }
        return new HashMap<>(headers);
    }

    public String getBody() {
        return body;
    }

}

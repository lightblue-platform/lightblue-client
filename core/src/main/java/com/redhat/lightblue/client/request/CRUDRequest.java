package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.Operation;

public abstract class CRUDRequest extends LightblueDataRequest {
    
    public CRUDRequest(HttpMethod method, String operationName, String entityName, String entityVersion) {
        super(method,operationName, entityName, entityVersion);
    }

    public abstract Operation getOperation();
}

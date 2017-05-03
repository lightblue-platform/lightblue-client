package com.redhat.lightblue.client.request;

import java.util.Set;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.Operation;

import com.redhat.lightblue.client.response.ResultMetadata;

public abstract class CRUDRequest extends LightblueDataRequest {

    protected boolean onlyIfCurrent;
    protected final Set<String> documentVersions=new HashSet<>();
    
    public CRUDRequest(HttpMethod method, String operationName, String entityName, String entityVersion) {
        super(method,operationName, entityName, entityVersion);
    }

    public CRUDRequest(CRUDRequest r) {
        super(r);
        onlyIfCurrent=r.onlyIfCurrent;
        documentVersions.addAll(r.documentVersions);
    }

    public boolean isIfCurrent() {
        return onlyIfCurrent;
    }

    public void setIfCurrent(boolean b) {
        this.onlyIfCurrent=b;
    }

    public void addDocumentVersions(String...ver) {
        for(String x:ver)
            documentVersions.add(x);
    }

    public void addDocumentVersions(ResultMetadata...ver) {
        for(ResultMetadata x:ver)
            documentVersions.add(x.getDocumentVersion());
    }

    protected void appendUpdateIfCurrentToJson(ObjectNode node) {
        if(onlyIfCurrent)
            node.set("onlyIfCurrent",JsonNodeFactory.instance.booleanNode(true));
        if(!documentVersions.isEmpty()) {
            ArrayNode arr=JsonNodeFactory.instance.arrayNode();
            for(String x:documentVersions) {
                arr.add(JsonNodeFactory.instance.textNode(x));
            }
            node.set("documentVersions",arr);
        }
    }

    public abstract Operation getOperation();
}

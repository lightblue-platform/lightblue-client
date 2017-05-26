package com.redhat.lightblue.client.response;

import java.io.Serializable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResultMetadata implements Serializable {

    private static final long serialVersionUID=-1;

    private String documentVersion;

    public String getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(String s) {
        documentVersion=s;
    }

    @Override
    public String toString() {
        return "v:"+documentVersion;
    }

    public static ResultMetadata fromJson(ObjectNode node) {
        ResultMetadata rmd=null;
        if(node!=null) {
            rmd=new ResultMetadata();
            JsonNode x=node.get("documentVersion");
            if(x instanceof TextNode) {
                rmd.documentVersion=x.asText();
            }
        }
        return rmd;
    }
}

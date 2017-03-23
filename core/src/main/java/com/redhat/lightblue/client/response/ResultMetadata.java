package com.redhat.lightblue.client.response;

import java.io.Serializable;

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
}

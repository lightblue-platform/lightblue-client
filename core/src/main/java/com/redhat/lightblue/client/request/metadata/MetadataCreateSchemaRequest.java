package com.redhat.lightblue.client.request.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;
import com.redhat.lightblue.client.util.JSON;
import com.redhat.lightblue.client.util.Utils;

/**
 * PUT /metadata/{entityName}/schema={version}
 */
public class MetadataCreateSchemaRequest extends AbstractLightblueMetadataRequest {

    private JsonNode body;

    public MetadataCreateSchemaRequest(String entityName, String entityVersion) {
        super(HttpMethod.PUT,"schema="+entityVersion,entityName,null);
    }

    @Override
    public JsonNode getBodyJson() {
        return body;
    }

    public void setBodyJson(JsonNode body) {
        this.body = body;
    }

    public void setBodyJson(String jsonString) {
        setBodyJson(JSON.toJsonNode(jsonString));
    }

    public void setBodyJson(InputStream in) throws IOException {
        setBodyJson(Utils.loadResource(in));
    }

    public void setBodyJson(Reader reader) throws IOException {
        setBodyJson(Utils.loadResource(reader));
    }

}

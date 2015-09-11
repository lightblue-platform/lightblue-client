package com.redhat.lightblue.client.request.metadata;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataCreateSchemaRequest extends AbstractLightblueMetadataRequest {

	private String schema;
    public MetadataCreateSchemaRequest() {
        super();
    }

    public MetadataCreateSchemaRequest(String entityName, String entityVersion, String schema) {
        super(entityName, entityVersion);
        this.schema = schema;
    }

    @Override
    public String getOperationPathParam() {
        return "";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.PUT;
    }

}

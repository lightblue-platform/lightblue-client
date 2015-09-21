package com.redhat.lightblue.client.request.metadata;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;
import com.redhat.lightblue.client.util.JSON;

public class MetadataCreateSchemaRequest extends AbstractLightblueMetadataRequest {

    public MetadataCreateSchemaRequest() {
        super();
    }

    public MetadataCreateSchemaRequest(String entityName, String entityVersion, String schema) {
        super(entityName, entityVersion);
        super.setBody(JSON.toJsonNode(schema));
    }
    
    public MetadataCreateSchemaRequest(String entityName, String entityVersion, JsonNode schema) {
        super(entityName, entityVersion);
        super.setBody(schema);
    }
    
    @Override
    public String getOperationPathParam() {
        return "";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.PUT;
    }
    
    
    @Override
    public String getRestURI(String baseServiceURI) {
    	StringBuilder requestURI = new StringBuilder();

        requestURI.append(baseServiceURI);

        if (StringUtils.isNotBlank(this.getEntityName())) {
            appendToURI(requestURI, this.getEntityName());
        }

    	if (StringUtils.isNotBlank(this.getEntityVersion())) {
            appendToURI(requestURI, "schema="+ this.getEntityVersion());
        }
    	return requestURI.toString();
    	
    }
    

}

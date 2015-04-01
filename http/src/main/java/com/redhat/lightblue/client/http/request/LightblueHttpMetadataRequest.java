package com.redhat.lightblue.client.http.request;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;
import com.redhat.lightblue.client.request.metadata.MetadataClearDefaultVersionRequest;
import com.redhat.lightblue.client.request.metadata.MetadataCreateRequest;
import com.redhat.lightblue.client.request.metadata.MetadataCreateSchemaRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityDependenciesRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityMetadataRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityNamesRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityRolesRequest;
import com.redhat.lightblue.client.request.metadata.MetadataGetEntityVersionsRequest;
import com.redhat.lightblue.client.request.metadata.MetadataRemoveEntityRequest;
import com.redhat.lightblue.client.request.metadata.MetadataSetDefaultVersionRequest;
import com.redhat.lightblue.client.request.metadata.MetadataUpdateEntityInfoRequest;
import com.redhat.lightblue.client.request.metadata.MetadataUpdateSchemaStatusRequest;

public class LightblueHttpMetadataRequest extends AbstractLightblueHttpRequest implements LightblueHttpRequest {

    private final AbstractLightblueMetadataRequest request;

    public LightblueHttpMetadataRequest(AbstractLightblueMetadataRequest request) {
        this.request = request;
    }

    @Override
    public HttpRequestBase getRestRequest(String baseServiceURI) {
        HttpRequestBase httpRequest = null;

        if (request instanceof MetadataClearDefaultVersionRequest) {
            return getHttpDelete(request.getRestURI(baseServiceURI));
        } else if (request instanceof MetadataCreateRequest) {
            return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
        } else if (request instanceof MetadataCreateSchemaRequest) {
            return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
        } else if (request instanceof MetadataGetEntityDependenciesRequest) {
            return getHttpGet(request.getRestURI(baseServiceURI));
        } else if (request instanceof MetadataGetEntityMetadataRequest) {
            return getHttpGet(request.getRestURI(baseServiceURI));
        } else if (request instanceof MetadataGetEntityNamesRequest) {
            return getHttpGet(request.getRestURI(baseServiceURI));
        } else if (request instanceof MetadataGetEntityRolesRequest) {
            return getHttpGet(request.getRestURI(baseServiceURI));
        } else if (request instanceof MetadataGetEntityVersionsRequest) {
            return getHttpGet(request.getRestURI(baseServiceURI));
        } else if (request instanceof MetadataRemoveEntityRequest) {
            return getHttpDelete(request.getRestURI(baseServiceURI));
        } else if (request instanceof MetadataSetDefaultVersionRequest) {
            return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
        } else if (request instanceof MetadataUpdateEntityInfoRequest) {
            return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
        } else if (request instanceof MetadataUpdateSchemaStatusRequest) {
            return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
        }
        return httpRequest;
    }

}

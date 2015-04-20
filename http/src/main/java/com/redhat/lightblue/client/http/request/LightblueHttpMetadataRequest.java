package com.redhat.lightblue.client.http.request;

import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.metadata.*;
import org.apache.http.client.methods.HttpRequestBase;

public class LightblueHttpMetadataRequest extends AbstractLightblueHttpRequest implements LightblueHttpRequest {

	LightblueRequest request;

	public LightblueHttpMetadataRequest(LightblueRequest request) {
		this.request = request;
	}

	@Override
	public HttpRequestBase getRestRequest(String baseServiceURI) {
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
		return null;
	}

}

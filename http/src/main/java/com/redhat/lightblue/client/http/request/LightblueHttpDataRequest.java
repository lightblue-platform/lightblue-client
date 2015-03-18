package com.redhat.lightblue.client.http.request;

import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.data.*;
import org.apache.http.client.methods.HttpRequestBase;

public class LightblueHttpDataRequest extends AbstractLightblueHttpRequest implements LightblueHttpRequest {

	LightblueRequest request;
	
	public LightblueHttpDataRequest(LightblueRequest request) {
		this.request = request;
	}
	
	@Override
	public HttpRequestBase getRestRequest(String baseServiceURI) {
		if (request instanceof DataDeleteRequest) {
			return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
		} else if (request instanceof DataFindRequest) {
			return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
		} else if (request instanceof DataInsertRequest) {
			return getHttpPut(request.getRestURI(baseServiceURI), request.getBody());
		} else if (request instanceof DataSaveRequest) {
			return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
		} else if (request instanceof DataUpdateRequest) {
			return getHttpPost(request.getRestURI(baseServiceURI), request.getBody());
		}
		return null;
	}

}

/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.http.request;

import org.apache.http.client.methods.HttpRequestBase;

import com.redhat.lightblue.client.request.LightblueRequest;
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

	LightblueRequest request;

	public LightblueHttpMetadataRequest(LightblueRequest request) {
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

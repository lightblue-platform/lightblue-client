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
import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.request.data.DataSaveRequest;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class LightblueHttpDataRequest extends AbstractLightblueHttpRequest implements LightblueHttpRequest {

	LightblueRequest request;
	
	public LightblueHttpDataRequest(LightblueRequest request) {
		this.request = request;
	}
	
	@Override
	public HttpRequestBase getRestRequest(String baseServiceURI) {
		HttpRequestBase httpRequest = null;

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
		return httpRequest;
	}

}

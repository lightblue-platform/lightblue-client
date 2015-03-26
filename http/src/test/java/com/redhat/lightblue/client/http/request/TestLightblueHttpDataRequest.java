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

import static com.redhat.lightblue.client.expression.query.ValueQuery.withValue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.http.request.stub.DataFindRequestStub;
import com.redhat.lightblue.client.http.request.stub.DataInsertRequestStub;
import com.redhat.lightblue.client.http.request.stub.DataSaveRequestStub;
import com.redhat.lightblue.client.http.request.stub.DataUpdateRequestStub;
import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.request.data.DataSaveRequest;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class TestLightblueHttpDataRequest extends AbstractLightblueHttpRequestTest {

	LightblueHttpDataRequest httpRequest;
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void testGetRestRequestWithDataDeleteRequest() throws IOException {
		DataDeleteRequest deleteRequest = new DataDeleteRequest();
		deleteRequest.where(withValue("_id = 1234abc"));
		httpRequest = new LightblueHttpDataRequest(deleteRequest);
		HttpPost expectedRequest = new HttpPost(baseURI + DataDeleteRequest.PATH_PARAM_DELETE);
		expectedRequest.setEntity(new StringEntity("{\"query\":{\"field\":\"_id\",\"op\":\"=\",\"rvalue\":\"1234abc\"}}"));
		HttpPost actualRequest = (HttpPost) httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataDeleteRequestWithEntityName() throws IOException {
		DataDeleteRequest deleteRequest = new DataDeleteRequest(entityName, null);
		deleteRequest.where(withValue("_id = 1234abc"));
		httpRequest = new LightblueHttpDataRequest(deleteRequest);
		HttpPost expectedRequest = new HttpPost(baseURI + DataDeleteRequest.PATH_PARAM_DELETE + "/" + entityName);
		expectedRequest.setEntity(new StringEntity("{\"query\":{\"field\":\"_id\",\"op\":\"=\",\"rvalue\":\"1234abc\"}}"));
		HttpPost actualRequest = (HttpPost) httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithDataDeleteRequestWithEntityNameAndVersion() throws IOException {
		DataDeleteRequest deleteRequest = new DataDeleteRequest(entityName, entityVersion);
		deleteRequest.where(withValue("_id = 1234abc"));
		httpRequest = new LightblueHttpDataRequest(deleteRequest);
		HttpPost expectedRequest = new HttpPost(baseURI + DataDeleteRequest.PATH_PARAM_DELETE + "/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity("{\"query\":{\"field\":\"_id\",\"op\":\"=\",\"rvalue\":\"1234abc\"}}"));
		HttpPost actualRequest = (HttpPost) httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	

	@Test
	public void testGetRestRequestWithDataFindRequest() throws UnsupportedEncodingException {
		HttpPost expectedRequest = new HttpPost(baseURI + DataFindRequest.PATH_PARAM_FIND);
		expectedRequest.setEntity(new StringEntity(body));
		DataFindRequest request = new DataFindRequestStub(null, null, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataFindRequestWithEntityName() throws UnsupportedEncodingException {
		DataFindRequest request = new DataFindRequestStub(entityName, null, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataFindRequest.PATH_PARAM_FIND +"/" + entityName);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataFindRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		DataFindRequest request = new DataFindRequestStub(entityName, entityVersion, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataFindRequest.PATH_PARAM_FIND +"/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPost actualRequest = (HttpPost) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataInsertRequest() throws IOException {
		DataInsertRequest request = new DataInsertRequestStub(null, null, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPut actualRequest = (HttpPut)httpRequest.getRestRequest(baseURI);
		compareHttpPut(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataInsertRequestWithEntityName() throws IOException {
		DataInsertRequest request = new DataInsertRequestStub(entityName, null, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName );
		expectedRequest.setEntity(new StringEntity(body));
		HttpPut actualRequest = (HttpPut)httpRequest.getRestRequest(baseURI);
		compareHttpPut(expectedRequest, actualRequest);	
	}
	
	@Test
	public void testGetRestRequestWithDataInsertRequestWithEntityNameAndVersion() throws IOException {
		DataInsertRequest request = new DataInsertRequestStub(entityName, entityVersion, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPut actualRequest = (HttpPut)httpRequest.getRestRequest(baseURI);
		compareHttpPut(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataSaveRequest() throws IOException {
		DataSaveRequest request = new DataSaveRequestStub(null, null, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataSaveRequest.PATH_PARAM_SAVE);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataSaveRequestWithEntityName() throws IOException {
		DataSaveRequest request = new DataSaveRequestStub(entityName, null, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataSaveRequest.PATH_PARAM_SAVE + "/" + entityName);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataSaveRequestWithEntityNameAndVersion() throws IOException {
		DataSaveRequest request = new DataSaveRequestStub(entityName, entityVersion, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataSaveRequest.PATH_PARAM_SAVE + "/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataUpdateRequest() throws IOException {
		DataUpdateRequest request = new DataUpdateRequestStub(null, null, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataUpdateRequest.PATH_PARAM_UPDATE);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataUpdateRequestWithEntityName() throws IOException {
		DataUpdateRequest request = new DataUpdateRequestStub(entityName, null, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataUpdateRequest.PATH_PARAM_UPDATE + "/" + entityName);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataUpdateRequestWithEntityNameAndVersion() throws IOException {
		DataUpdateRequest request = new DataUpdateRequestStub(entityName, entityVersion, body);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataUpdateRequest.PATH_PARAM_UPDATE + "/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
}

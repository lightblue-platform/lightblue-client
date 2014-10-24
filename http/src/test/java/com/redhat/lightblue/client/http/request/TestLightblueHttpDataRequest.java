package com.redhat.lightblue.client.http.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;

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
	public void testGetRestRequestWithDataDeleteRequest() {
		DataDeleteRequest deleteRequest = new DataDeleteRequest();
		httpRequest = new LightblueHttpDataRequest(deleteRequest);
		HttpDelete expectedRequest = new HttpDelete(baseURI + DataDeleteRequest.PATH_PARAM_DELETE);
		HttpDelete actualRequest = (HttpDelete) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataDeleteRequestWithEntityName() {
		DataDeleteRequest deleteRequest = new DataDeleteRequest(entityName, null);
		httpRequest = new LightblueHttpDataRequest(deleteRequest);
		HttpDelete expectedRequest = new HttpDelete(baseURI + DataDeleteRequest.PATH_PARAM_DELETE + "/" + entityName);
		HttpDelete actualRequest = (HttpDelete) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithDataDeleteRequestWithEntityNameAndVersion() {
		DataDeleteRequest deleteRequest = new DataDeleteRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpDataRequest(deleteRequest);
		HttpDelete expectedRequest = new HttpDelete(baseURI + DataDeleteRequest.PATH_PARAM_DELETE + "/" + entityName + "/" + entityVersion);
		HttpDelete actualRequest = (HttpDelete) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	

	@Test
	public void testGetRestRequestWithDataFindRequest() throws UnsupportedEncodingException {
		HttpPost expectedRequest = new HttpPost(baseURI + DataFindRequest.PATH_PARAM_FIND);
		expectedRequest.setEntity(new StringEntity(body));
		DataFindRequest request = new DataFindRequest();
		httpRequest = new LightblueHttpDataRequest(request);
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataFindRequestWithEntityName() throws UnsupportedEncodingException {
		DataFindRequest request = new DataFindRequest(entityName, null);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataFindRequest.PATH_PARAM_FIND +"/" + entityName);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataFindRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		DataFindRequest request = new DataFindRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataFindRequest.PATH_PARAM_FIND +"/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataInsertRequest() throws IOException {
		DataInsertRequest request = new DataInsertRequest();
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPut actualRequest = (HttpPut)httpRequest.getRestRequest(baseURI);
		compareHttpPut(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataInsertRequestWithEntityName() throws IOException {
		DataInsertRequest request = new DataInsertRequest(entityName, null);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName );
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPut actualRequest = (HttpPut)httpRequest.getRestRequest(baseURI);
		compareHttpPut(expectedRequest, actualRequest);	
	}
	
	@Test
	public void testGetRestRequestWithDataInsertRequestWithEntityNameAndVersion() throws IOException {
		DataInsertRequest request = new DataInsertRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPut actualRequest = (HttpPut)httpRequest.getRestRequest(baseURI);
		compareHttpPut(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataSaveRequest() throws IOException {
		DataSaveRequest request = new DataSaveRequest();
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataSaveRequest.PATH_PARAM_SAVE);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataSaveRequestWithEntityName() throws IOException {
		DataSaveRequest request = new DataSaveRequest(entityName, null);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataSaveRequest.PATH_PARAM_SAVE + "/" + entityName);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataSaveRequestWithEntityNameAndVersion() throws IOException {
		DataSaveRequest request = new DataSaveRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataSaveRequest.PATH_PARAM_SAVE + "/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataUpdateRequest() throws IOException {
		DataUpdateRequest request = new DataUpdateRequest();
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataUpdateRequest.PATH_PARAM_UPDATE);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataUpdateRequestWithEntityName() throws IOException {
		DataUpdateRequest request = new DataUpdateRequest(entityName, null);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataUpdateRequest.PATH_PARAM_UPDATE + "/" + entityName);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithDataUpdateRequestWithEntityNameAndVersion() throws IOException {
		DataUpdateRequest request = new DataUpdateRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpDataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + DataUpdateRequest.PATH_PARAM_UPDATE + "/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)httpRequest.getRestRequest(baseURI);
		compareHttpPost(expectedRequest, actualRequest);
	}
	
}

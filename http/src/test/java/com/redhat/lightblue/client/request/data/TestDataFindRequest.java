package com.redhat.lightblue.client.request.data;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import com.redhat.lightblue.client.request.data.DataFindRequest;

public class TestDataFindRequest extends AbstractLightblueRequestTest  {

	DataFindRequest request;

	@Before
	public void setUp() throws Exception {
		request = new DataFindRequest();
	}

	@Test
	public void testGetRestRequest() throws UnsupportedEncodingException {
		HttpPost expectedRequest = new HttpPost(baseURI + DataFindRequest.PATH_PARAM_FIND);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithEntityName() throws UnsupportedEncodingException {
		request = new DataFindRequest(entityName, null);
		HttpPost expectedRequest = new HttpPost(baseURI + DataFindRequest.PATH_PARAM_FIND +"/" + entityName);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}
	
	@Test
	public void testGetRestRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		request = new DataFindRequest(entityName, entityVersion);
		HttpPost expectedRequest = new HttpPost(baseURI + DataFindRequest.PATH_PARAM_FIND +"/" + entityName + "/" + entityVersion);
		expectedRequest.setEntity(new StringEntity(body));
		request.setBody(body);
		HttpPost actualRequest = (HttpPost)request.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataFindRequest.PATH_PARAM_FIND, request.getOperationPathParam());
	}

}

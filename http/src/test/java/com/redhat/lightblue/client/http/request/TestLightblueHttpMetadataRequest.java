package com.redhat.lightblue.client.http.request;

import com.redhat.lightblue.client.request.metadata.*;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class TestLightblueHttpMetadataRequest extends AbstractLightblueHttpRequestTest {

	LightblueHttpMetadataRequest httpRequest;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testGetRestRequestWithMetadataClearDefaultVersionRequest() {
		MetadataClearDefaultVersionRequest request = new MetadataClearDefaultVersionRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpDelete expectedRequest = new HttpDelete(baseURI);
		HttpDelete actualRequest = (HttpDelete) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataClearDefaultVersionRequestWithEntityName() {
		MetadataClearDefaultVersionRequest request = new MetadataClearDefaultVersionRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpDelete expectedRequest = new HttpDelete(baseURI + entityName);
		HttpDelete actualRequest = (HttpDelete) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataClearDefaultVersionRequestWithEntityNameAndVersion() {
		MetadataClearDefaultVersionRequest request = new MetadataClearDefaultVersionRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpDelete expectedRequest = new HttpDelete(baseURI + entityName + "/" + entityVersion);
		HttpDelete actualRequest = (HttpDelete) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataCreateRequest() throws UnsupportedEncodingException {
		MetadataCreateRequest request = new MetadataCreateRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataCreateRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataCreateRequest request = new MetadataCreateRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataCreateRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataCreateRequest request = new MetadataCreateRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName + "/" + entityVersion);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataCreateSchemaRequest() throws UnsupportedEncodingException {
		MetadataCreateSchemaRequest request = new MetadataCreateSchemaRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataCreateSchemaRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataCreateSchemaRequest request = new MetadataCreateSchemaRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataCreateSchemaRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataCreateSchemaRequest request = new MetadataCreateSchemaRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName + "/" + entityVersion);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityDependenciesRequest() throws UnsupportedEncodingException {
		MetadataGetEntityDependenciesRequest request = new MetadataGetEntityDependenciesRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + MetadataGetEntityDependenciesRequest.PATH_PARAM_GET_ENTITY_DEPENDENCIES);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityDependenciesRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataGetEntityDependenciesRequest request = new MetadataGetEntityDependenciesRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + MetadataGetEntityDependenciesRequest.PATH_PARAM_GET_ENTITY_DEPENDENCIES);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityDependenciesRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataGetEntityDependenciesRequest request = new MetadataGetEntityDependenciesRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion + "/"
		    + MetadataGetEntityDependenciesRequest.PATH_PARAM_GET_ENTITY_DEPENDENCIES);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityMetadataRequest() throws UnsupportedEncodingException {
		MetadataGetEntityMetadataRequest request = new MetadataGetEntityMetadataRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityMetadataRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataGetEntityMetadataRequest request = new MetadataGetEntityMetadataRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityMetadataRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataGetEntityMetadataRequest request = new MetadataGetEntityMetadataRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityNamesRequest() throws UnsupportedEncodingException {
		MetadataGetEntityNamesRequest request = new MetadataGetEntityNamesRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityNamesRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataGetEntityNamesRequest request = new MetadataGetEntityNamesRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityNamesRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataGetEntityNamesRequest request = new MetadataGetEntityNamesRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityRolesRequest() throws UnsupportedEncodingException {
		MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + MetadataGetEntityRolesRequest.PATH_PARAM_GET_ENTITY_ROLES);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityRolesRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + MetadataGetEntityRolesRequest.PATH_PARAM_GET_ENTITY_ROLES);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityRolesRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataGetEntityRolesRequest request = new MetadataGetEntityRolesRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion + "/" + MetadataGetEntityRolesRequest.PATH_PARAM_GET_ENTITY_ROLES);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityVersionsRequest() throws UnsupportedEncodingException {
		MetadataGetEntityVersionsRequest request = new MetadataGetEntityVersionsRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityVersionsRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataGetEntityVersionsRequest request = new MetadataGetEntityVersionsRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataGetEntityVersionsRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataGetEntityVersionsRequest request = new MetadataGetEntityVersionsRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpGet expectedRequest = new HttpGet(baseURI + entityName + "/" + entityVersion);
		HttpGet actualRequest = (HttpGet) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataRemoveEntityRequest() throws UnsupportedEncodingException {
		MetadataRemoveEntityRequest request = new MetadataRemoveEntityRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpDelete expectedRequest = new HttpDelete(baseURI);
		HttpDelete actualRequest = (HttpDelete) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataRemoveEntityRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataRemoveEntityRequest request = new MetadataRemoveEntityRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpDelete expectedRequest = new HttpDelete(baseURI + entityName);
		HttpDelete actualRequest = (HttpDelete) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataRemoveEntityRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataRemoveEntityRequest request = new MetadataRemoveEntityRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpDelete expectedRequest = new HttpDelete(baseURI + entityName + "/" + entityVersion);
		HttpDelete actualRequest = (HttpDelete) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataSetDefaultVersionRequest() throws UnsupportedEncodingException {
		MetadataSetDefaultVersionRequest request = new MetadataSetDefaultVersionRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + MetadataSetDefaultVersionRequest.PATH_PARAM_SET_DEFAULT_VERSION);
		request.setBody(body);
		HttpPost actualRequest = (HttpPost) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataSetDefaultVersionRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataSetDefaultVersionRequest request = new MetadataSetDefaultVersionRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + entityName + "/" + MetadataSetDefaultVersionRequest.PATH_PARAM_SET_DEFAULT_VERSION);
		request.setBody(body);
		HttpPost actualRequest = (HttpPost) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataSetDefaultVersionRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataSetDefaultVersionRequest request = new MetadataSetDefaultVersionRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPost expectedRequest = new HttpPost(baseURI + entityName + "/" + entityVersion + "/" + MetadataSetDefaultVersionRequest.PATH_PARAM_SET_DEFAULT_VERSION);
		request.setBody(body);
		HttpPost actualRequest = (HttpPost) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataUpdateEntityInfoRequest() throws UnsupportedEncodingException {
		MetadataUpdateEntityInfoRequest request = new MetadataUpdateEntityInfoRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataUpdateEntityInfoRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataUpdateEntityInfoRequest request = new MetadataUpdateEntityInfoRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataUpdateEntityInfoRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataUpdateEntityInfoRequest request = new MetadataUpdateEntityInfoRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName + "/" + entityVersion);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataUpdateSchemaStatusRequest() throws UnsupportedEncodingException {
		MetadataUpdateSchemaStatusRequest request = new MetadataUpdateSchemaStatusRequest();
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataUpdateSchemaStatusRequestWithEntityName() throws UnsupportedEncodingException {
		MetadataUpdateSchemaStatusRequest request = new MetadataUpdateSchemaStatusRequest(entityName, null);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

	@Test
	public void testGetRestRequestWithMetadataUpdateSchemaStatusRequestWithEntityNameAndVersion() throws UnsupportedEncodingException {
		MetadataUpdateSchemaStatusRequest request = new MetadataUpdateSchemaStatusRequest(entityName, entityVersion);
		httpRequest = new LightblueHttpMetadataRequest(request);
		HttpPut expectedRequest = new HttpPut(baseURI + entityName + "/" + entityVersion);
		request.setBody(body);
		HttpPut actualRequest = (HttpPut) httpRequest.getRestRequest(baseURI);
		compareHttpRequestBase(expectedRequest, actualRequest);
	}

}

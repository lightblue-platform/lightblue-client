package com.redhat.lightblue.client.request;


public class AbstractLightblueRequestTest {

	protected static final String entityName = "lightblueEntity";
	protected static final String entityVersion = "1.2.3";
	protected static final String body = "{\"name\":\"value\"}";
	protected static final String baseURI = "http://lightblue.io/rest/";
	protected static final String dataOperation = "dosomethingwithdata";
	protected static final String metadataOperation = "dosomethingwithmetadata";
	protected static final String finalDataURI = baseURI + dataOperation + "/" + entityName + "/" + entityVersion;
	protected static final String finalMetadataURI = baseURI + entityName + "/" + entityVersion + "/" + metadataOperation;
	
}

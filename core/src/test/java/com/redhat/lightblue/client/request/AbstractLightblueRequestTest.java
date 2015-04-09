package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest.DataOperation;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest.MetadataOperation;

public class AbstractLightblueRequestTest {

    protected static final String entityName = "lightblueEntity";
    protected static final String entityVersion = "1.2.3";
    protected static final String body = "{\"name\":\"value\"}";
    protected static final String baseURI = "http://lightblue.io/rest/";
    protected static final DataOperation dataOperation = DataOperation.FIND;
    protected static final MetadataOperation metadataOperation = MetadataOperation.GET_ENTITY_DEPENDENCIES;
    protected static final String finalDataURI = baseURI + dataOperation.getPathParam() + "/" + entityName + "/" + entityVersion;
    protected static final String finalMetadataURI = baseURI + entityName + "/" + entityVersion + "/" + metadataOperation.getPathParam();

}

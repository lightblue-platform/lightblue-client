package com.redhat.lightblue.client.test.request;

import com.redhat.lightblue.client.request.data.DataFindRequest;

/**
 * Created by jblashka on 11/3/14.
 */
public class DataFindRequestStub extends DataFindRequest {
    private String body;
    public DataFindRequestStub(String entityName, String entityVersion, String body){
        setEntityName(entityName);
        setEntityVersion(entityVersion);
        this.body = body;
    }
    public String getBody(){
        return body;
    }

}

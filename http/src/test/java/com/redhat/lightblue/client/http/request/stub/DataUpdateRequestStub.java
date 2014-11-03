package com.redhat.lightblue.client.http.request.stub;

import com.redhat.lightblue.client.request.data.DataUpdateRequest;

/**
 * Created by jblashka on 10/20/14.
 */
public class DataUpdateRequestStub extends DataUpdateRequest {
    private String body;
    public DataUpdateRequestStub(String entityName, String entityVersion, String body){
        setEntityName(entityName);
        setEntityVersion(entityVersion);
        this.body = body;
    }
    public String getBody(){
        return body;
    }
}

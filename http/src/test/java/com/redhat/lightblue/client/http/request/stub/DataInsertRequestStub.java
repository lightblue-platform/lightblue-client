package com.redhat.lightblue.client.http.request.stub;

import com.redhat.lightblue.client.request.data.DataInsertRequest;

/**
 * Created by jblashka on 10/20/14.
 */
public class DataInsertRequestStub extends DataInsertRequest {
    private String body;
    public DataInsertRequestStub(String entityName, String entityVersion, String body){
        setEntityName(entityName);
        setEntityVersion(entityVersion);
        this.body = body;
    }
    @Override
    public String getBody() {
        return body;
    }
}

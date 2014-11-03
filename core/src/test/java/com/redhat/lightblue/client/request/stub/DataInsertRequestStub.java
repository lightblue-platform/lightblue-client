package com.redhat.lightblue.client.request.stub;

import com.redhat.lightblue.client.request.DataInsertRequest;

/**
 * Created by jblashka on 10/20/14.
 */
public class DataInsertRequestStub extends DataInsertRequest {
    private String body;
    public DataInsertRequestStub(String body){
        this.body = body;
    }
    @Override
    public String getBody() {
        return body;
    }
}

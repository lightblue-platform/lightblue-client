package com.redhat.lightblue.client.request.stub;

import com.redhat.lightblue.client.request.DataUpdateRequest;

/**
 * Created by jblashka on 10/20/14.
 */
public class DataUpdateRequestStub extends DataUpdateRequest {
    private String body;
    public DataUpdateRequestStub(String body){
        this.body = body;
    }
    public String getBody(){
        return body;
    }
}

package com.redhat.lightblue.client.http.request.stub;

import com.redhat.lightblue.client.request.data.DataDeleteRequest;

/**
 * Created by jblashka on 10/20/14.
 */
public class DataDeleteRequestStub extends DataDeleteRequest {
    private String body;
    public DataDeleteRequestStub(String body){
        this.body = body;
    }
    public String getBody(){
        return body;
    }
}

package com.redhat.lightblue.client.test.request;

import com.redhat.lightblue.client.request.data.DataDeleteRequest;

/**
 * Created by jblashka on 10/20/14.
 */
public class DataDeleteRequestStub extends DataDeleteRequest {
    private final String body;

    public DataDeleteRequestStub(String entityName, String entityVersion, String body) {
        super(entityName, entityVersion);
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }

}
